package com.circleappsstudio.mimisa.utils

import android.app.Activity
import android.content.Context
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

    private val permissions by lazy { Permissions() }

    private val date by lazy {
        SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa", Locale.US)
                .format(Calendar.getInstance().time)
    }

    /*var PERMISSIONS = arrayOf(
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
    }*/

    /*fun hasPermissionsForSeatListReport(seatList: List<Seat>) {
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
    }*/

    fun printSeatListPDFReport(seatList: List<Seat>): Boolean {
        return if (permissions.hasReadAndWritePermission(context, *permissions.PERMISSIONS)) {
            seatListReportPDF(seatList)
            true
        } else {
            ActivityCompat.requestPermissions(activity, permissions.PERMISSIONS, permissions.PERMISSION_ALL)
            false
        }
    }

    fun printIntentionListPDFReport(intentionList: List<Intention>) {
        if (permissions.hasReadAndWritePermission(context, *permissions.PERMISSIONS)) {
            intentionListReportPDF(intentionList)
        } else {
            ActivityCompat.requestPermissions(activity, permissions.PERMISSIONS, permissions.PERMISSION_ALL)
        }
    }

    fun seatListReportPDF(seatList: List<Seat>) {

        val file = File(context.getExternalFilesDir("/"), "reporte_asientos_reservados_${date}.pdf")

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
                        context.getString(R.string.report_title),
                        DeviceRgb(50, 115, 168),
                        true,
                        25F,
                        TextAlignment.CENTER
                )
        )

        document.add(
                createParagraph(
                        //"Fecha de generación: $date",
                        "${context.getString(R.string.report_date)} $date",
                        DeviceRgb(0, 0, 0),
                        false,
                        15F,
                        TextAlignment.LEFT
                )

        )

        document.add(
                createParagraph(
                        context.getString(R.string.seat_list),
                        DeviceRgb(50, 115, 168),
                        true,
                        22F,
                        TextAlignment.CENTER
                )

        )

        seatList.forEach { seat ->

            table.addCell(
                    createParagraph(
                            //"N.º Asiento: ${seat.seatNumber}",
                            "${context.getString(R.string.seat_number_cell)} ${seat.seatNumber}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

            table.addCell(
                    createParagraph(
                            //"Nombre: \n${seat.nameUser} ${seat.lastNameUser}",
                            "${context.getString(R.string.name_cell)} \n${seat.nameUser} ${seat.lastNameUser}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

            table.addCell(
                    createParagraph(
                            //"Cédula: \n${seat.idNumberUser}",
                            "${context.getString(R.string.id_number_cell)} \n${seat.idNumberUser}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

            table.addCell(
                    createParagraph(
                            "${context.getString(R.string.assistance_cell)} \nSí (  )   No (  )",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )

            )

        }

        document.add(table)

        document.close()

        Toast.makeText(context, "${context.getString(R.string.report_created_successfully)} $file", Toast.LENGTH_LONG).show()

    }

    fun intentionListReportPDF(intentionList: List<Intention>) {

        val file = File(context.getExternalFilesDir("/"), "reporte_intenciones_registradas_${date}.pdf")

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
                        context.getString(R.string.report_title),
                        DeviceRgb(50, 115, 168),
                        true,
                        25F,
                        TextAlignment.CENTER
                )
        )

        document.add(
                createParagraph(
                        "${context.getString(R.string.report_date)} $date",
                        DeviceRgb(0, 0, 0),
                        false,
                        15F,
                        TextAlignment.LEFT
                )

        )

        document.add(
                createParagraph(
                        context.getString(R.string.intention_list),
                        DeviceRgb(50, 115, 168),
                        true,
                        22F,
                        TextAlignment.CENTER
                )

        )

        intentionList.forEach { i ->

            table.addCell(
                    createParagraph(
                            //"Categoría: \n${i.category}",
                            "${context.getString(R.string.category_cell)} \n${i.category}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

            table.addCell(
                    createParagraph(
                            //"Intención: \n${i.intention}",
                            "${context.getString(R.string.intention_cell)} \n${i.intention}",
                            DeviceRgb(0, 0, 0),
                            false,
                            15F,
                            TextAlignment.CENTER
                    )
            )

        }

        document.add(table)

        document.close()

        Toast.makeText(context, "${context.getString(R.string.report_created_successfully)} $file", Toast.LENGTH_LONG).show()

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

        val drawable: Drawable = getDrawable(context, R.drawable.mimisa_banner)!!
        val bitmap = (drawable as BitmapDrawable).bitmap

        val stream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)

        val image = Image(imageData)
        image.setHeight(80F)
        image.setWidth(350F)
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