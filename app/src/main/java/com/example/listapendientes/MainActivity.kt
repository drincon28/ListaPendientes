package com.example.listapendientes

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.OutputStreamWriter
import java.io.PrintStream
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var listItem: MutableList<String>
    var datafileName: String = "my_todo_list.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //listItem = resources.getStringArray(R.array.todo_list_items).toMutableList()
        if ( File( baseContext.getFileStreamPath(datafileName).getAbsolutePath() ).exists()) {
            listItem = readMyFile2()
        }else {
            listItem = mutableListOf()
        }

        val btnAdd = findViewById<Button>(R.id.btn_add_todo)
        val inputNumber = findViewById<TextInputEditText>(R.id.input_todo)
        val listView = findViewById<ListView>(R.id.todo_list)
        //var textView = findViewById<TextView>(R.id.todo_text_view)

        val adapter = ArrayAdapter<String>(
            this,
            R.layout.list_view_layout,
            R.id.todo_text_view,
            listItem
        )

        for (element in listItem) {
            println(element)
        }

        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, position, l ->
                val value = "Has click sostenido para borrar: " + adapter.getItem(position)
                Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
            }

        listView.onItemLongClickListener =
            OnItemLongClickListener { adapterView, view, position, l ->
                val msg = "Se eliminó: " + adapter.getItem(position).toString()
                listItem.removeAt(position)
                writeMyFile(listItem)

                adapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                true
            }

        btnAdd.setOnClickListener {
            val nuevaTarea: String = input_todo.text.toString()
            val msg = "Se agregó: $nuevaTarea"

            listItem.add(nuevaTarea)
            writeMyFile(listItem)

            adapter.notifyDataSetChanged()

            input_todo.setText("")

            //Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }

        inputNumber.setOnClickListener {
        }
    }

    /* Este método solo funciona para lectura es el res/raw */
    private fun readMyFile(): MutableList<String>{
        //val scan = Scanner(openFileInput("my_todo_list.txt"))
        val scan = Scanner( resources.openRawResource(R.raw.my_todo_list))
        var itemsList: MutableList<String> = mutableListOf()

        while (scan.hasNextLine()) {
            val line = scan.nextLine()
            itemsList.add(line)
        }

        return itemsList
    }

    private fun readMyFile2(): MutableList<String>{
        val scan = Scanner(openFileInput(datafileName))

        var itemsList: MutableList<String> = mutableListOf()

        while (scan.hasNextLine()) {
            val line = scan.nextLine()
            itemsList.add(line)
        }

        return itemsList
    }

    private fun writeMyFile(itemsList: MutableList<String>): Boolean{
        val outputFile =  openFileOutput("my_todo_list.txt", MODE_PRIVATE)
        val outputWriter = OutputStreamWriter(outputFile)

        itemsList.forEach { item ->
            println(item)
            outputFile.write("$item\n".toByteArray())
        }

        outputWriter.close()
        Toast.makeText(getBaseContext(), "Archivo guardado con exito!", Toast.LENGTH_SHORT).show();

        println(baseContext.getFileStreamPath("my_todo_list.txt").getAbsolutePath())

        return true
    }
}