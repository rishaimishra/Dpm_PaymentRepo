package com.dpm.payment.kotlinHelper

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.os.CountDownTimer
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt

import androidx.core.content.ContextCompat

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment

import com.google.android.material.button.MaterialButton

import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ExFun {
    companion object {




        fun EditText.setErrorFun(error: String) {
            this.error = error
            requestFocus()
        }

        fun EditText.setDrawable(
            left: Int = 0,
            top: Int = 0,
            right: Int = 0,
            bottom: Int = 0
        ) {
            setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }

        fun TextView.setDrawable(
            left: Int = 0,
            top: Int = 0,
            right: Int = 0,
            bottom: Int = 0
        ) {
            setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }


        fun View.handleVisibility() {
            var data = ""
            if (this is TextView)
                data = this.text.toString()

            if (data.isEmpty()) this.visibility = View.GONE
        }

        fun EditText.setMultiLineActionDone() {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        }

        fun EditText.disableGesture() {
            isLongClickable = false
            isClickable = false
            isFocusable = false
            isFocusableInTouchMode = false
        }

        fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                var timer: CountDownTimer? = null

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    timer?.cancel()
                    timer = object : CountDownTimer(3500, 3000) {
                        override fun onTick(millisUntilFinished: Long) {}
                        override fun onFinish() {
                            afterTextChanged.invoke(editable.toString())
                        }
                    }.start()
                }
            })
        }

        fun TextView.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }


        fun EditText.onAction(action: Int, runAction: () -> Unit) {
            this.setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    action -> {
                        runAction.invoke()
                        true
                    }

                    else -> false
                }
            }
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        fun Fragment.showKeyboard() {
            view?.let { activity?.showKeyboard(it) }
        }

        fun Activity.showKeyboard() {
            showKeyboard(currentFocus ?: View(this))
        }


        fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun Context.showKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, 0)
        }


        fun DrawerLayout.disableGesture() {
            this.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        }

        fun DrawerLayout.enableGesture() {
            this.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        }

        fun Spinner.selected(action: (position: Int, parent: AdapterView<*>?) -> Unit) {
            this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    action(position, parent)
                }
            }
        }

        fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

        fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

        // DATE TIME UTILS
        fun Date.toStringDate(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }

        // file operation extension function
        fun File.convertToBase64(): String {
            return android.util.Base64.encodeToString(this.readBytes(), android.util.Base64.NO_WRAP)
        }

        fun Bitmap.encodeImage(): String? {
            val baos = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT)
        }

        fun String.decodeBitmap(): Bitmap? {
            return try {
                val imageBytes = Base64.decode(this, 0)
                val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                image
            } catch (e: Exception) {
                null
            }
        }

        // Date picker extension

        fun Context.showDatePicker(
            beforeData: Boolean = true,
            futureDate: Boolean = false,
            selectedDate: (String) -> Unit
        ) {
            val c = Calendar.getInstance()
            val yr = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val display = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val monthInput = String.format("%02d", (monthOfYear + 1))
                    val day = String.format("%02d", dayOfMonth)
                    selectedDate.invoke("$day-$monthInput-$year")
                },
                yr,
                month,
                day
            )
            if (beforeData) display.datePicker.maxDate =
                System.currentTimeMillis() - (24 * 60 * 60 * 1000)
            if (futureDate) display.datePicker.minDate =
                System.currentTimeMillis() + (24 * 60 * 60 * 1000)
            display.show()
        }

        fun Drawable.overrideColor(@ColorInt colorInt: Int) {
            when (this) {
                is GradientDrawable -> setColor(colorInt)
                is ShapeDrawable -> paint.color = colorInt
                is ColorDrawable -> color = colorInt
            }
        }


        fun Context.showPopupMenu(
            view: View,
            menuLayout: Int,
            onItemClick: (item: MenuItem) -> Unit
        ) {
            val popup = PopupMenu(this, view)
            popup.inflate(menuLayout)

            popup.setOnMenuItemClickListener { item: MenuItem? ->
                item?.let { onItemClick.invoke(it) }
                /* when (item!!.itemId) {
                     R.id.menuRemove -> {
                         clickInterface.onRemoveItem(position, dataItem)
                     }

                 }*/

                true
            }

            popup.show()
        }

        fun String.toUserName() = "@$this"
        fun String.toUsdCurrency() = "$ $this"




        fun Context.openUrl(link: String) =
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))

        fun Activity.openNetworkSettings() {
            startActivity(
                Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            )
        }


        fun TextView.changeTextColor(color: Int) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    color
                )
            )
        }


    }
}



