package com.mvvm.example.utilities

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.mvvm.example.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaManager {
    companion object {


        private const val CREATE_FILE_PATTERN = "yyyyMMdd_HHmmssSSS"

        fun createTmpFileForPic(context: Context): File {
            val timeStamp =
                SimpleDateFormat(CREATE_FILE_PATTERN, Locale.getDefault()).format(
                    Date()
                )
            val mImageName = "MB_$timeStamp.jpg"
            val dir: File? =
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            return File(dir, mImageName)
        }

        private fun getFileName(context: Context, uri: Uri?): String? {
            var cursor: Cursor? = null
            val projection = arrayOf(
                MediaStore.MediaColumns.DISPLAY_NAME
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, null, null,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }


        fun pickOnlineGooglePhotos(context: Context, uri: Uri?): String? {
            var outputFile: File? = null
            var fileName: String? = null
            try {
                fileName = getFileName(context, uri)
            } catch (e: Exception) {
                Toast.makeText(context, "Exception fileName " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
            if (fileName != null) {
                try {
                    val googlePhotos = context.contentResolver.openFileDescriptor(uri!!, "r")
                    val fileInputStream = FileInputStream(googlePhotos!!.fileDescriptor)
                    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    if (storageDir?.exists() == false) {
                        storageDir.mkdirs()
                    }
                    outputFile = File(storageDir, fileName)
                    val outputStream = FileOutputStream(outputFile)
                    val buffer = ByteArray(4 * 1024) // buffer size
                    while (true) {
                        val byteCount = fileInputStream.read(buffer)
                        if (byteCount < 0) break
                        outputStream.write(buffer, 0, byteCount)
                    }
                    outputStream.flush()
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(context, "IOException " + e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "file name empty", Toast.LENGTH_SHORT).show()
            }
            return outputFile?.absolutePath
        }

        fun getAppPath(activity: Context): File {

            val outputDirectory: File?
            val doYouWantToSavePhotoOnDevice = true
            if (doYouWantToSavePhotoOnDevice) {
                val dcim_dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                if (!dcim_dir.exists()) {
                    dcim_dir.mkdirs()
                }
                outputDirectory = File(dcim_dir, activity.getString(R.string.app_name))
                if (!outputDirectory.exists()) {
                    outputDirectory.mkdirs()
                }
            } else {
                outputDirectory = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                if (!outputDirectory!!.exists()) {
                    outputDirectory.mkdirs()
                }
            }
            val PREFIX = "SF_"
            val PHOTO_EXTENSION = ".jpg"
            return File(
                outputDirectory, PREFIX + SimpleDateFormat(CREATE_FILE_PATTERN, Locale.getDefault())
                    .format(System.currentTimeMillis()) + PHOTO_EXTENSION
            )
        }
    }
}