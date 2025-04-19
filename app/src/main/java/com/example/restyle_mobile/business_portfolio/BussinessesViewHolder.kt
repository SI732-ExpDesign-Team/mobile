package com.example.restyle_mobile

import Beans.Businesses
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.restyle_mobile.R

class BussinessesViewHolder(view:View):RecyclerView.ViewHolder(view) {
    var businessId=view.findViewById<TextView>(R.id.bussinessId)
    var businessesDescription=view.findViewById<TextView>(R.id.txtBusinessDescription)
    var businessesPhoto=view.findViewById<ImageView>(R.id.imgBusiness)

    fun render(businessesModel: Businesses){
        businessId.text=businessesModel.id.toString()
        businessesDescription.text=businessesModel.description

        // Usando Glide para cargar la imagen desde la URL
        Glide.with(businessesPhoto.context)
            .load(businessesModel.image) // URL de la imagen
            .placeholder(R.drawable.ic_launcher_background) // Imagen por defecto mientras carga
            .error(R.drawable.ic_launcher_foreground) // Imagen en caso de error
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: Configura el caché de disco
            .into(businessesPhoto)
    }
}