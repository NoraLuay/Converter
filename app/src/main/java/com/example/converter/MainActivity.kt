package com.example.converter
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private val egyptianPound ="Egyptian Pound";
    private val americanDollar ="American Dollar";
    private val Euro ="Euro";

    private lateinit var toDropDownMenu : AutoCompleteTextView;
    private lateinit var fromDropDownMenu : AutoCompleteTextView;
    private lateinit var converterBtn: Button;
    private lateinit var amountETxt: TextInputEditText;
    private lateinit var resultEditText: TextInputEditText;
    private lateinit var toolBar: Toolbar;

    private val values: Map<String, Double> = mapOf(
        egyptianPound to 15.37,
        americanDollar to 1.0,
        Euro to 0.88
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialViews();
        populateDropDownMenu();
        amountETxt.addTextChangedListener{
            calculateResult();
        }

        fromDropDownMenu.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                calculateResult();
            }
        })

        toDropDownMenu.setOnItemClickListener { parent, view, position, id -> calculateResult() }

//        converterBtn.setOnClickListener {
//            calculateResult();
//        }


        toolBar.inflateMenu(R.menu.options_menu)
        toolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.share_action) {

                val myMassage = "${amountETxt.text.toString()}  ${fromDropDownMenu.text} "+ "is equal to ${resultEditText.text.toString()} ${toDropDownMenu.text}"
                Log.v("myWTF", myMassage)
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, myMassage)
                if(shareIntent.resolveActivity(packageManager)!=null){
                    startActivity(shareIntent)
                }
                }else{
                    Toast.makeText(this , "no activity found to handle the intent", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }

    private fun calculateResult(){
        if(amountETxt.text.toString().isNotEmpty()){
            val amount = amountETxt.text.toString().toDouble()

            val toValueKey = toDropDownMenu.text.toString()
            val toValue = values[toValueKey]
            Log.v("wtf", "toValue: $toValue")

            val fromValueKey = fromDropDownMenu.text.toString()
            val fromValue = values[fromValueKey]
            Log.v("wtf", "fromValue: $fromValue")

            val result = amount.times(toValue!!).div(fromValue!!);
            val formatResult = String.format("%.2f", result);
            Log.v("wtf", "formatResult: $formatResult");
            resultEditText.setText(formatResult);
        }else{
            //Snackbar.make(amountETxt, "amount field required", Snackbar.LENGTH_SHORT).show();
            amountETxt.error = "amount field required";
            resultEditText.setText("");

        }
    }
    private fun populateDropDownMenu(){
        val listOfCountry = listOf(egyptianPound, americanDollar, Euro);
        val adapter = ArrayAdapter(this,R.layout.drop_down_list_item, listOfCountry);
        toDropDownMenu.setAdapter(adapter);
        fromDropDownMenu.setAdapter(adapter);

    }
    private fun initialViews(){
        converterBtn= findViewById(R.id.converter_button);
        amountETxt= findViewById(R.id.amount_Edit_text);
        resultEditText = findViewById(R.id.result_edit_text);
        toDropDownMenu = findViewById(R.id.to_currency_menu);
        fromDropDownMenu= findViewById(R.id.from_auto_complete_text_view);
        toolBar= findViewById(R.id.tool_bar);
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val menuInflater : MenuInflater = menuInflater
//        menuInflater.inflate(R.menu.options_menu, menu);
//        return true;
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.share_action){
//
//        }else if (item.itemId == R.id.setting_action){
//
//        }else{
//
//        }
//        return true;
//    }
}
