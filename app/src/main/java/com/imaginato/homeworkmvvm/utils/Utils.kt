package com.imaginato.homeworkmvvm.utils

import android.content.Context
import android.widget.Toast

/**
 * Created by Parth Soni on 12/8/2022.
 */
object Utils {

    fun showErrorToast(context:Context, strErrorMsg: String?) {
        if (strErrorMsg.isNullOrEmpty()) {
            Toast.makeText(context,strErrorMsg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,strErrorMsg,Toast.LENGTH_SHORT).show()
        }
    }
}