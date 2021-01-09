package com.jay.baytalk.extension

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Activity.showToaster(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
internal fun Fragment.showToaster(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}