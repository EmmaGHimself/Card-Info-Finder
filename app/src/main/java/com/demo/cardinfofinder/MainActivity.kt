package com.demo.cardinfofinder

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager.BadTokenException
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.demo.cardinfofinder.MainActivity.UIStateViewModel.UIState.DATA_FOUND
import com.demo.cardinfofinder.MainActivity.UIStateViewModel.UIState.DEFAULT
import com.demo.cardinfofinder.MainActivity.UIStateViewModel.UIState.LOADING
import com.demo.cardinfofinder.MainActivity.UIStateViewModel.UIState.NO_DATA
import com.demo.cardinfofinder.utils.*
import com.demo.restapi.CardInfoFinderCloud
import com.demo.restapi.models.CardInfoDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    var textValue: String? = null

    var uiStateViewModel: UIStateViewModel? = null

    private var customLayout: View? = null

    var progressDialog: ProgressDialog? = null

    var cardInfoDetails: CardInfoDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = intent
        textValue = intent.getStringExtra(INTENT_TEXT_VALUE)

        textValue?.let { loadCardDetail(it) }

        uiStateViewModel = ViewModelProviders.of(this).get(UIStateViewModel::class.java)

        setUpViewModels()

        etCardNumber!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, count: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isEmpty()) {
                    cardNumber!!.text = "**** **** **** ****"
                } else {
                    val number = Tools.insertPeriodically(
                        charSequence.toString().trim { it <= ' ' }, " ", 4
                    )
                    cardNumber!!.text = number
                }
            }

            override fun afterTextChanged(editable: Editable) {
                //To append logo to card ui
                if (editable.isNotEmpty()) {
                    cardProviderLogo!!.visibility = VISIBLE
                    //handled in utils to set card logo
                    cardProviderLogo!!.setImageResource(getCartLogo(editable))
                } else {
                    cardProviderLogo!!.visibility = GONE
                }

                //Logic to space card number
                setCardNumber(editable)
                if (editable.isNotEmpty()) {
                    cardNumber!!.text = editable.toString()
                } else {
                    cardNumber!!.text = getString(R.string.card_number)
                }

                //Get card details from server when edit text completed
                postCardDetailsToServer(editable)
            }
        })
    }

    /*** This function method controls the state of the UI ***/
    private fun setUpViewModels() {
        uiStateViewModel?.stateUI?.observe(this, { state ->
            when (state) {
                DEFAULT -> {

                }
                LOADING -> {
                    //determines the UI state when the app is loading data
                    main?.visibility = GONE
                    progress?.visibility = VISIBLE
                }
                DATA_FOUND -> {
                    //determines the UI state when the app has found data
                    val cardBrand = customLayout?.findViewById<TextView>(R.id.card_brand)
                    val cardType = customLayout?.findViewById<TextView>(R.id.card_type)
                    val bankName = customLayout?.findViewById<TextView>(R.id.bank_name)
                    val cardCurrency = customLayout?.findViewById<TextView>(R.id.currency)
                    val card = customLayout?.findViewById<TextView>(R.id.cardType)
                    val bankUrl = customLayout?.findViewById<TextView>(R.id.bank_url)
                    val country = customLayout?.findViewById<TextView>(R.id.country)


                    if (cardInfoDetails != null) {

                        if (cardInfoDetails!!.bank?.url != null && cardInfoDetails!!.bank != null) {
                            val url: String? = cardInfoDetails!!.bank?.url
                            if (bankUrl != null) {
                                bankUrl.text = url
                            }
                        } else {
                            bankUrl?.visibility = GONE
                        }

                        if (cardInfoDetails!!.brand != null) {
                            val brand: String? = cardInfoDetails!!.brand
                            if (cardBrand != null) {
                                cardBrand.text = brand?.capitalize(Locale.ROOT)
                            }
                        } else {
                            cardBrand?.setText(R.string.not_available)
                        }

                        if (cardInfoDetails!!.type != null) {
                            val type: String? = cardInfoDetails!!.type
                            if (cardType != null) {
                                cardType.text = type?.capitalize(Locale.ROOT)
                            }
                            if (card != null) {
                                card.text = type?.capitalize(Locale.ROOT) + " " + "Card"
                            }
                        } else {
                            cardType?.setText(R.string.not_available)
                            card?.setText(R.string.not_available)
                        }

                        if (cardInfoDetails!!.bank?.name != null) {
                            val bank: String? = cardInfoDetails!!.bank?.name
                            if (bankName != null) {
                                bankName.text = bank
                            }
                        } else {
                            bankName?.setText(R.string.not_available)
                        }

                        if (cardInfoDetails!!.country?.currency != null) {
                            val currency: String? = cardInfoDetails!!.country?.currency
                            if (cardCurrency != null) {
                                cardCurrency.text = currency
                            }
                        } else {
                            cardCurrency?.setText(R.string.not_available)
                        }

                        if (cardInfoDetails!!.country?.name != null) {
                            val country_name: String? = cardInfoDetails!!.country?.name
                            if (country != null) {
                                country.text = country_name
                            }
                        } else {
                            cardCurrency?.setText(R.string.not_available)
                        }

                        if (cardInfoDetails!!.scheme == ("visa")) {
                            customLayout?.findViewById<ImageView>(R.id.card_logo)?.setImageResource(
                                R.drawable.ic_visa
                            )
                        }
                        if (cardInfoDetails!!.scheme == ("mastercard")) {
                            customLayout?.findViewById<ImageView>(R.id.card_logo)?.setImageResource(
                                R.drawable.ic_mastercard
                            )
                        }

                        if (cardInfoDetails!!.scheme == ("discover")) {
                            customLayout?.findViewById<ImageView>(R.id.card_logo)?.setImageResource(
                                R.drawable.ic_discover
                            )
                        }

                        if (cardInfoDetails!!.scheme == ("amex")) {
                            customLayout?.findViewById<ImageView>(R.id.card_logo)?.setImageResource(
                                R.drawable.ic_amex
                            )
                        }

                        etCardNumber?.text?.clear()

                    }
                }
                NO_DATA -> {
                    //determines the UI state when there is no data to load.
                    val dialog = Dialog(this@MainActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                    dialog.setContentView(R.layout.dialog_warning)
                    dialog.setCancelable(false)

                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT


                    (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener {
                        dialog.dismiss()
                        main?.visibility = VISIBLE
                        etCardNumber?.text?.clear()
                    }

                    dialog.show()
                    dialog.window!!.attributes = lp

                    etCardNumber?.text?.clear()
                }
            }
        })
    }

    class UIStateViewModel : ViewModel() {
        private val uiState = MutableLiveData<Int>()

        val stateUI: LiveData<Int> = uiState

        internal object UIState {
            const val DEFAULT = 0
            const val LOADING = 1
            const val DATA_FOUND = 2
            const val NO_DATA = 3

        }

        fun setUIState(state: Int) {
            uiState.postValue(state)
            Log.d(TAG, "UI State: '$state' Set.")
        }

        companion object {
            private const val TAG = "UIStateVM"
        }

        init {
            uiState.postValue(DEFAULT)
        }
    }

    private fun postCardDetailsToServer(s: Editable) {
        if (s.length == 7 || s.length == 9) {
            val k: String = cardNumber?.text.toString().replace(" ", "")

            //Show user progress bar before posting to the server
            loadCardDetail(k)
        }
    }

    private fun loadCardDetail(k: String) {
        etCardNumber?.hideKeyboard()
        main?.visibility = GONE
        progress?.visibility = VISIBLE

        val builder = AlertDialog.Builder(this@MainActivity).create()

        customLayout = layoutInflater.inflate(R.layout.dialog_card_info, null)
        builder.setView(customLayout)
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        CardInfoFinderCloud.getInstance(applicationContext)?.getCardInfoDetails(
            k,
            object : Callback<CardInfoDetails?> {
                override fun onResponse(
                    call: Call<CardInfoDetails?>,
                    response: Response<CardInfoDetails?>
                ) {
                    if (response.code() == 200) {
                        main?.visibility = VISIBLE
                        progress?.visibility = GONE
                        cardInfoDetails = response.body()


                        try {
                            builder.show()
                        } catch (ex: BadTokenException) {
                            ex.printStackTrace()
                        }


                        uiStateViewModel?.setUIState(DATA_FOUND)
                        customLayout?.findViewById<FloatingActionButton>(R.id.fab)
                            ?.setOnClickListener {
                                overridePendingTransition(
                                    android.R.anim.slide_in_left,
                                    android.R.anim.slide_out_right
                                )
                                try {
                                    builder.dismiss()
                                } catch (ex: BadTokenException) {
                                    ex.printStackTrace()
                                }

                            }
                    } else {
                        progress?.visibility = GONE
                        uiStateViewModel?.setUIState(NO_DATA)
                    }
                }

                override fun onFailure(call: Call<CardInfoDetails?>, t: Throwable) {
                    t.printStackTrace()
                    progress?.visibility = GONE
                    etCardNumber?.text?.clear()
                    Toast.makeText(
                        this@MainActivity,
                        "Network Connection Error!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun buttonStart(view: View?) {
        val intent = Intent(applicationContext, OcrCaptureActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        const val INTENT_TEXT_VALUE: String = "text_value"
    }


}