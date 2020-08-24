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
        const ee = "신규유저는 아닙니다";
        // return snapshot.ref.parent.child('uppercase').set(uppercase);
        return snapshot.ref.parent.parent.parent.parent.child('Users').child(context.auth.uid)
            .update({'statusMessage': ee})
});

exports.sendFCM = functions.database.ref('/Message/{}/{pushId}/')
    .onCreate((snapshot, context) => {
        const msName = snapshot.child('name').val().toString();
        const msMessage = snapshot.child('message').val().toString();
        const msTime = snapshot.child('time').val().toString();
        console.log('Name', msName);
        console.log('Time', msMessage);
        const roomName = snapshot.ref.parent.key;   // 방번호가 나옴
        console.log('FCM roomname', roomName);

        // console.log('FCM1', snapshot.ref.parent.parent.parent.child("UserInRoom")
        //     .child(roomName).get);
        // console.log('FCM2', snapshot.ref.parent.parent.parent.child("UserInRoom").child(roomName).child().key);
        // return snapshot.ref.parent.parent.parent.parent.child('Users').child(context.auth.uid)
        //     .update({'statusMessage': ee})
        const promiseRoomUserList = admin.database()
            .ref(`UserInRoom/${roomName}`).once('value'); // 채팅방 유저리스트

        return Promise.all([promiseRoomUserList]).then(results => {
            const userRoomSnapShot = results[0]
            const arrRoomUserList =[];
            if(userRoomSnapShot.hasChildren()){
                userRoomSnapShot.forEach(snapshot => {
                    arrRoomUserList.push(snapshot.key);
                return null}
                )
            }else{
                return console.log('RoomUserlist is null')
            }

            const arrTargetUserList = arrRoomUserList;

            for(let i=0; i < arrTargetUserList.length; i++){
                admin.database().ref(`FcmId/${arrTargetUserList[i]}`).once("value",function(snapshot) {
                    const token = snapshot.val();
                    if (token) {
                        var message = {
                            data: {
                                title: msName,
                                body: msMessage,
                                time: msTime
                            },
                            token: token
                        };

// Send a message to the device corresponding to the provided
// registration token.
                        admin.messaging().send(message)
                            .then((response) => {
                                // Response is a message ID string.
                                console.log('Successfully sent message:', response);
                                return null
                            })
                            .catch((error) => {
                                console.log('Error sending message:', error);
                                return null
                            });
                        //메세지발송
                        // admin.messaging().sendToDevice(token, payload).then(response => {
                        //     response.results.forEach((result, index) => {
                        //         const error = result.error;
                        //         if (error) {
                        //             console.error('FCM 실패 :', error.code);
                        //         }else{
                        //             console.log('FCM 성공');
                        //         }
                        //     });
                        //     return null
                        // });
                    }
                });
            }
            return null
        }, reason => {
            console.log('fail', reason)
        });



        //2차
        // var dbRecording = snapshot.ref.parent.parent.parent.child('UserInRoom/'+roomName);
        // admin.database().ref(`UserInRoom/${roomName}`).once("value", function(snapshot) {
        //     snapshot.forEach(function(child) {
        //         console.log(child.key+": "+child.val());
        //         admin.database().ref(`FcmId/${child.key}`).once('value', function(snapshot){
        //
        //             console.log("FVCKYOU", snapshot.val())
        //     });
        //
        //     });
        // });
        //2차

//         var registrationToken = "dM1_YRfrTX-S-sTAijzJPY:APA91bH5vHEyGUrfZsuucHPa27ld6J6Bj_4tXrc2YSYmY5ggtPaHogT2Hbx9tjmnVpuj2YZsIhsBTsecEsIEn30GQmLTXxQPC_yJqT3gtuVfr0OAmCikk0z5m5-MekV2UUO__mQlZLUe"
//         // This registration token comes from the client FCM SDKs.
//
//         var message = {
//             data: {
//                 title: "test!!",
//                 body: "TEST ING!!",
//                 time: '2:45'
//             },
//             token: registrationToken
//         };
//
// // Send a message to the device corresponding to the provided
// // registration token.
//         admin.messaging().send(message)
//             .then((response) => {
//                 // Response is a message ID string.
//                 console.log('Successfully sent message:', response);
//                 return null
//             })
//             .catch((error) => {
//                 console.log('Error sending message:', error);
//                 return null
//             });
    //여까지
    // var payload = {
    //     notification: {
    //         title: "TEST!!",
    //         body: "TESt ING"
    //     }
    // }
    //
    // var result = admin.messaging().sendToDevice(token, payload).then((response) => {
    //     // Response is a message ID string.
    //     console.log('Successfully sent message:', response);
    //     return null
    // })
    //     .catch((error) => {
    //         console.log('Error sending message:', error);
    // });
    // return result;
});