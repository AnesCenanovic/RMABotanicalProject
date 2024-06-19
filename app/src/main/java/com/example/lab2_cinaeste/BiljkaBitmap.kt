package com.example.lab2_cinaeste

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream
import java.io.Serializable


@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [ForeignKey(entity = Biljka::class, parentColumns = ["id"], childColumns = ["idBiljke"], onDelete = ForeignKey.CASCADE)]
)
@TypeConverters(BitmapConverter::class)
data class BiljkaBitmap (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idBiljke: Int = 0,
    @TypeConverters(BitmapConverter::class) var bitmap: Bitmap
)

class BitmapConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        if (originalWidth <= 400 && originalHeight <= 400) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            return Base64.encodeToString(byteArray,Base64.DEFAULT)
        }
        else {
            val resizedBmp = Bitmap.createScaledBitmap(bitmap, 400, 400, true)
            val stream = ByteArrayOutputStream()
            resizedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            return Base64.encodeToString(byteArray,Base64.DEFAULT)
        }
    }

    @TypeConverter
    fun toBitmap(string: String): Bitmap {
        val byteArray = Base64.decode(string,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
