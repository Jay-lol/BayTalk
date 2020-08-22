'use strict';

const functions = require('firebase-functions');
const sanitizer = require('./sanitizer');
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
    functions.logger.info("Hello logs!", {structuredData: true});
    response.send("Hello from Firebase!");
});

exports.addMessage = functions.https.onCall((data, context) => {
    // [START_EXCLUDE]
    // [START readMessageData]
    // Message text passed from the client.
    const text = data.text;
    // [END readMessageData]
    // [START messageHttpsErrors]
    // Checking attribute.
    if (!(typeof text === 'string') || text.length === 0) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
            'one arguments "text" containing the message text to add.');
    }
    // Checking that the user is authenticated.
    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
            'while authenticated.');
    }
    // [END messageHttpsErrors]

    // [START authIntegration]
    // Authentication / user information is automatically added to the request.
    const uid = context.auth.uid;
    const name = context.auth.token.name || null;
    const picture = context.auth.token.picture || null;
    const email = context.auth.token.email || null;
    // [END authIntegration]

    // [START returnMessageAsync]
    // Saving the new message to the Realtime Database.
    const sanitizedMessage = sanitizer.sanitizeText(text); // Sanitize the message.
    return admin.database().ref('/messages').push({
        text: sanitizedMessage,
        author: {uid, name, picture, email},
    }).then(() => {
        console.log('New Message written');
        // Returning the sanitized message to the client.
        return {text: sanitizedMessage};
    })
        // [END returnMessageAsync]
        .catch((error) => {
            // Re-throwing the error as an HttpsError so that the client gets the error details.
            throw new functions.https.HttpsError('unknown', error.message, error);
        });
    // [END_EXCLUDE]
});

// exports.addMessage = functions.https.onRequest(async (req, res) => {
//   // Grab the text parameter.
//   const original = req.query.text;
//   // Push the new message into Cloud Firestore using the Firebase Admin SDK.
//   const writeResult = await admin.database().ref('/message/').push({original: original});
//   // Send back a message that we've succesfully written the message
//   res.json({result: `Message with ID: ${writeResult.id} added.`});
// });

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/Message/{}/{pushId}/name')
    .onCreate((snapshot, context) => {
        // Grab the current value of what was written to the Realtime Database.
        const original = snapshot.val();
        // console.log('Uppercasing', context.resource.name, original);
        const x = snapshot.ref.parent.parent.key;   // 방번호가 나옴
        console.log('Uppercasing',x, original);
        const uppercase = original.toUpperCase();
        // You must return a Promise when performing asynchronous tasks inside a Functions such as
        // writing to the Firebase Realtime Database.
        // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
        // snapshot.ref.child('RoomUser').orderByChild('roomId').equalTo(x).on()
        //
        // return snapshot.ref.parent.parent.parent.parent.child('RoomUser')
        //     .child.child(x).child('lastMessage').update(uppercase);
        const ee = "Beyou";
        // return snapshot.ref.parent.child('uppercase').set(uppercase);
        return snapshot.ref.parent.parent.parent.parent.child('Users').child(context.auth.uid)
            .update({'statusMessage': ee})
    });