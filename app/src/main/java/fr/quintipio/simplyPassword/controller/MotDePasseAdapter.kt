package fr.quintipio.simplyPassword.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.model.MotDePasse

class MotDePasseAdapter(context: Context, mdps: List<MotDePasse>) : ArrayAdapter<MotDePasse>(context, 0, mdps) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_mdp, parent, false)
        }

        var viewHolder: MotDePasseViewHolder? = convertView!!.tag as MotDePasseViewHolder
        if (viewHolder == null) {
            viewHolder = MotDePasseViewHolder()
            viewHolder.titre = convertView.findViewById(R.id.titreMdp)
            viewHolder.login = convertView.findViewById(R.id.loginMdp)
            convertView.tag = viewHolder
        }

        val motDePasse = getItem(position)

        viewHolder.titre!!.text = motDePasse!!.titre
        viewHolder.login!!.text = motDePasse.login

        return convertView
    }


    private inner class MotDePasseViewHolder {
        var titre: TextView? = null
        var login: TextView? = null
    }
}
