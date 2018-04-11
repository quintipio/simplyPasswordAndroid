package fr.quintipio.simplyPassword.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import fr.quintipio.simplyPassword.R
import fr.quintipio.simplyPassword.model.Dossier

class FolderAdapter(context: Context, dossiers: MutableList<Dossier>) : ArrayAdapter<Dossier>(context, 0, dossiers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_folder, parent, false)
        }

        var viewHolder: FolderViewHolder? = convertView!!.tag as FolderViewHolder
        if (viewHolder == null) {
            viewHolder = FolderViewHolder()
            viewHolder.nomDossier = convertView.findViewById(R.id.nomDossier)
            convertView.tag = viewHolder
        }

        val dossier = getItem(position)

        viewHolder.nomDossier!!.text = dossier!!.titre

        return convertView
    }

    private inner class FolderViewHolder {
        var nomDossier: TextView? = null
    }
}
