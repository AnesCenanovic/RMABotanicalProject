package com.example.lab2_cinaeste

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream


@Entity(
    tableName = "BiljkaBitmap",
    foreignKeys = [ForeignKey(entity = Biljka::class, parentColumns = ["id"], childColumns = ["biljkaId"], onDelete = ForeignKey.CASCADE)]
)
@TypeConverters(BitmapConverter::class)
data class BiljkaBitmap (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val biljkaId: Int = 0,
    @TypeConverters(BitmapConverter::class) var bitmap: Bitmap
)

class BitmapConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val resizedBmp: Bitmap = Bitmap.createBitmap(bitmap, 0, 0, 400, 400)
        val stream = ByteArrayOutputStream()
        resizedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream) // Adjust quality as needed
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
