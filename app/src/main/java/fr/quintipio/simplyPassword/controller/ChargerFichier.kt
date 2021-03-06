package fr.quintipio.simplyPassword.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.business.PasswordBusiness
import fr.quintipio.simplyPassword.contexte.ContexteAppli
import fr.quintipio.simplyPassword.util.InvalidPasswordException
import kotlinx.android.synthetic.main.activity_charger_fichier.*
import java.io.File
import java.util.*

class ChargerFichier : AppCompatActivity() {

    private var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_charger_fichier)
        fileButton.setOnClickListener { chargerFichier() }
        ChargerButton.setOnClickListener { dechiffrer() }
        MotDePasseTextField.setOnEditorActionListener { _, _, _ ->
            dechiffrer()
            true
        }

        val settings = getSharedPreferences(ContexteAppli.sharedPrefKey, 0)
        path = settings.getString(ContexteAppli.filePrefKey, null)

        if (path != null) {
            if (File(path).exists()) {
                fileTextView.text = path
                MotDePasseFrame.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Lance le file picker pour charger un fichier en mèmoire, en demandant l'autorisation
     */
    private fun chargerFichier() {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            val requestCode = 0
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
        } else {
            val fileType = arrayOf(".spj")
            val listeFormat = ArrayList<String>()
            listeFormat.add(".spj")
            FilePickerBuilder.getInstance().setMaxCount(10)
                    .setActivityTheme(R.style.AppTheme)
                    .addFileSupport("SPJ", fileType)
                    .pickFile(this)
        }
    }

    /**
     * Charge un fichier dnas l'appli
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val docPaths = ArrayList<String>()
            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS))

            if (docPaths[0].endsWith(".spj")) {
                fileTextView.text = docPaths[0]
                MotDePasseFrame.visibility = View.VISIBLE
                path = docPaths[0]
            } else {
                MotDePasseFrame.visibility = View.INVISIBLE
                path = ""
            }
        }
    }

    /**
     * Lance le déchiffrement d'un fichier
     */
    private fun dechiffrer() {
        val mdp = MotDePasseTextField.text.toString()
        try {
            PasswordBusiness.load(path!!,mdp)
            ErreurTextView.text = ""

            val settings = getSharedPreferences(ContexteAppli.sharedPrefKey, 0)
            val editor = settings.edit()
            editor.putString(ContexteAppli.filePrefKey, path)
            editor.commit()

            val listeMdp = Intent(this, ListeMotDePasse::class.java)
            startActivity(listeMdp)

        } catch (ex: InvalidPasswordException) {
            ErreurTextView.setText(R.string.erreur_mauvais_mdp)
        } catch (e: Exception) {
            System.out.print(e.stackTrace)
            ErreurTextView.setText(R.string.erreur_dechiffrement)
        }

        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}
