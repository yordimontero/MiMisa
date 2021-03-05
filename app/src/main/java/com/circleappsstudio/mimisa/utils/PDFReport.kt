package com.circleappsstudio.mimisa.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.circleappsstudio.mimisa.data.model.Seat
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PDFReport(private val context: Context, private val activity: Activity) {

    /*fun printPDF(numeroAsiento: String, context: Context, seatList: List<Seat>) {

        var seatNumber = ""

        val pdfDocument by lazy {
            PdfDocument()
        }

        val paint by lazy {
            Paint()
        }

        val forLinePaint by lazy {
            Paint()
        }

        val pageInfo: PdfDocument.PageInfo = PdfDocument.
        PageInfo.Builder(250, 350, 1).create()

        val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)

        val canvas by lazy {
            page.canvas
        }

        paint.textSize = 15.5F
        paint.color = Color.BLACK

        canvas.drawText("Este es el título", 20F, 20F, paint)

        paint.textSize = 8.5F

        canvas.drawText("Subtítulo 1", 20F, 40F, paint)
        canvas.drawText("Subtítulo 2", 20F, 55F, paint)

        forLinePaint.style = Paint.Style.STROKE
        //forLinePaint.pathEffect = DashPathEffect(Float[]{5.5}, 0F)
        forLinePaint.strokeWidth = 2F
        canvas.drawLine(20F, 65F, 230F, 65F, forLinePaint)

        canvas.drawText("Asiento: $numeroAsiento", 20F, 80F, paint)
        canvas.drawLine(20F, 90F, 230F, 90F, forLinePaint)

        //canvas.drawText("XDXD", x, y, paint)

        seatList.forEach { i ->

            canvas.drawLine(20F, y, 230F, y, forLinePaint)

            y += 20F

            canvas.drawText("N.º Asiento: ${i.seatNumber}", x, y, paint)

            y += 20F

            canvas.drawText("Nombre: ${i.nameUser}", x, y, paint)

            y += 20F

            canvas.drawText("Apellidos: ${i.lastNameUser}", x, y, paint)

            y += 20F

            canvas.drawText("Cédula: ${i.idNumberUser}", x, y, paint)

            //y += 20F

        }

        pdfDocument.finishPage(page)
        val file = File(context.getExternalFilesDir("/"), "Test.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: IOException){
            e.printStackTrace()
        }

        pdfDocument.close()

    }*/

    var PERMISSIONS = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var PERMISSION_ALL = 12
    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
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

    fun hasPermissions(seatList: List<Seat>) {
        if (hasPermissions(context, *PERMISSIONS)) {
            printPDF(seatList)
        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
        }
    }

    fun printPDF(seatList: List<Seat>) {

        val date by lazy {
            SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa", Locale.US).format(Calendar.getInstance().time)
        }

        val pdfPath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()

        val file = File(pdfPath, "Test.pdf")
        val outputStream = FileOutputStream(file)


        val pdfWriter = PdfWriter(file)
        val pdfDocument = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        var paragraph: Paragraph


        val line = SolidLine(1f)
        line.color = ColorConstants.BLACK

        val ls = LineSeparator(line)
        ls.setWidth(500F)





        paragraph = Paragraph("Diaconía La Argentina - Parroquia de Tacares")
        paragraph.setFontColor(ColorConstants.BLUE)
        paragraph.setBold()
        paragraph.setFontSize(25F)
        document.add(paragraph)

        paragraph = Paragraph("Fecha de generación: $date")
        paragraph.setBold()
        paragraph.setFontSize(15F)
        document.add(paragraph)


        paragraph = Paragraph("Lista de Asientos")
        paragraph.setFontColor(ColorConstants.BLUE)
        paragraph.setBold()
        paragraph.setTextAlignment(TextAlignment.CENTER)
        paragraph.setFontSize(22F)
        document.add(paragraph)

        seatList.forEach { i ->

            paragraph = Paragraph("N.º Asiento: ${i.seatNumber}")
            paragraphStyle(paragraph)
            document.add(ls)
            document.add(paragraph)


            paragraph = Paragraph("Nombre: ${i.nameUser}")
            paragraphStyle(paragraph)
            document.add(paragraph)

            paragraph = Paragraph("Apellidos: ${i.lastNameUser}")
            paragraphStyle(paragraph)
            document.add(paragraph)

            paragraph = Paragraph("Cédula: ${i.idNumberUser}")
            paragraphStyle(paragraph)
            document.add(paragraph)

            paragraph = Paragraph("Asistencia: Sí (  )   No (  )")
            paragraphStyle(paragraph)
            document.add(paragraph)

        }

        document.close()

        Toast.makeText(context, "Documento creado", Toast.LENGTH_SHORT).show()

    }

    fun paragraphStyle(paragraph: Paragraph) {
        paragraph.setFontSize(15F)
    }

}