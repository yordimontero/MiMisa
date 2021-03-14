package com.circleappsstudio.mimisa.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import com.circleappsstudio.mimisa.R
import com.circleappsstudio.mimisa.data.model.Intention
import com.circleappsstudio.mimisa.data.model.Seat
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PDFReport(private val context: Context, private val activity: Activity) {

    private val date by lazy {
        SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa", Locale.US)
                .format(Calendar.getInstance().time)
    }

    var PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var PERMISSION_ALL = 12

    fun hasPermissionsForSeatListReport(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                                context,
                                permission!!
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun hasPermissionsForSeatListReport(seatList: List<Seat>) {
        if (hasPermissionsForSeatListReport(context, *PERMISSIONS)) {
            printSeatListReportPDF(seatList)
        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
        }
    }

    fun hasPermissionsForIntentionListReport(intentionList: List<Intention>) {
        if (hasPermissionsForSeatListReport(context, *PERMISSIONS)) {
            printIntentionListReportPDF(intentionList)
        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
        }
    }


    fun printSeatListReportPDF(seatList: List<Seat>) {

        //Escribir el archivo en la ruta "Downloads". (Deprecado).
        /*val pdfPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()*/
        //val file = File(pdfPath, "Test.pdf")

        val file = File(context.getExternalFilesDir("/"), "reporte_asientos_reservados_${date}.pdf")
        //val outputStream = FileOutputStream(file)

        val pdfWriter = PdfWriter(file)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val columnWidth = floatArrayOf(100F, 300F, 140F, 150F)
        val table = Table(columnWidth)

        document.add(
                setImage()
        )

        document.add(
                createParagraph(
                        "Diaconía La Argentina - Parroquia de Tacares",
                        DeviceRgb(50, 115, 168),
                        true,
                        25F,
                        TextAlignment.CENTER
                )
        )

        document.add(
                createParagraph(
                        "Fecha de generación: $date",
                        DeviceRgb(0, 0, 0),
                        false,
                        15F,
                        TextAlignment.LEFT
                )

        )

        document.add(
                createParagraph(
                        "Lista de Asientos",
                        DeviceRgb(50, 115, 168),
                        true,
                        22F,
                        TextAlignment.CENTER
                )

        )

        seatList.forEach { i ->

            table.addCell(
                    createParagraph(
                            "N.º Asiento: ${i.seatNumber}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

            table.addCell(
                    createParagraph(
                            "Nombre: \n${i.nameUser} ${i.lastNameUser}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

            table.addCell(
                    createParagraph(
                            "Cédula: \n${i.idNumberUser}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

            table.addCell(
                    createParagraph(
                            "Asistencia: \nSí (  )   No (  )",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

        }

        document.add(table)

        document.close()

        Toast.makeText(context, "Reporte generado correctamente en la ruta: $file", Toast.LENGTH_LONG).show()

    }

    fun printIntentionListReportPDF(intentionList: List<Intention>) {

        //Escribir el archivo en la ruta "Downloads". (Deprecado).
        /*val pdfPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()*/
        //val file = File(pdfPath, "Test.pdf")

        val file = File(context.getExternalFilesDir("/"), "reporte_intenciones_registradas_${date}.pdf")
        //val outputStream = FileOutputStream(file)

        val pdfWriter = PdfWriter(file)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        val columnWidth = floatArrayOf(160F, 530F)
        val table = Table(columnWidth)

        document.add(
                setImage()
        )

        document.add(
                createParagraph(
                        "Diaconía La Argentina - Parroquia de Tacares",
                        DeviceRgb(50, 115, 168),
                        true,
                        25F,
                        TextAlignment.CENTER
                )
        )

        document.add(
                createParagraph(
                        "Fecha de generación: $date",
                        DeviceRgb(0, 0, 0),
                        false,
                        15F,
                        TextAlignment.LEFT
                )

        )

        document.add(
                createParagraph(
                        "Lista de Intenciones",
                        DeviceRgb(50, 115, 168),
                        true,
                        22F,
                        TextAlignment.CENTER
                )

        )

        intentionList.forEach { i ->

            table.addCell(
                    createParagraph(
                            "Categoría: \n${i.category}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

            table.addCell(
                    createParagraph(
                            "Intención: \n${i.intention}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

        }

        document.add(table)

        document.close()

        Toast.makeText(context, "Reporte generado correctamente en la ruta: $file", Toast.LENGTH_LONG).show()

    }

    fun createParagraph(
            text: String,
            deviceRgb: DeviceRgb,
            setBold: Boolean,
            fontSize: Float,
            textAlignment: TextAlignment
    ): Paragraph {

        val paragraph = Paragraph(text)

        paragraph.setFontColor(deviceRgb)

        if (setBold) {
            paragraph.setBold()
        }

        paragraph.setFontSize(fontSize)

        paragraph.setTextAlignment(textAlignment)

        return paragraph

    }

    fun setImage(): Image {

        val drawable: Drawable = getDrawable(context, R.drawable.templo_la_argentina_report)!!
        val bitmap = (drawable as BitmapDrawable).bitmap

        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)

        val image = Image(imageData)
        image.setHeight(100F)
        image.setWidth(150F)
        image.setHorizontalAlignment(HorizontalAlignment.CENTER)

        return image

    }

    fun createSeparatorLine(): LineSeparator {

        val solidLine = SolidLine(1f)
        solidLine.color = ColorConstants.BLACK

        val lineSeparator = LineSeparator(solidLine)
        lineSeparator.setWidth(500F)
        return lineSeparator

    }

}