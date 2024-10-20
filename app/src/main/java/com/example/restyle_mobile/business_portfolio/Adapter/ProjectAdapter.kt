package Adapter

import Beans.Projects
import Persistence.OpenHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.restyle_mobile.ProjectsViewHolder
import com.example.restyle_mobile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectAdapter(val projects:List<Projects>):RecyclerView.Adapter<ProjectsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        lateinit var dbHelper: OpenHelper
        val project = projects[position]
        holder.render(project)
        val deleteIcon = holder.itemView.findViewById<ImageView>(R.id.delete_icon)
        deleteIcon.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                // Realiza la operación de eliminación en un hilo de entrada/salida
                dbHelper = OpenHelper(holder.itemView.context)
                dbHelper.deleteProject(project.id)

                // Actualiza la interfaz de usuario en el hilo principal
                withContext(Dispatchers.Main) {
                    projects.toMutableList().remove(project)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                    Toast.makeText(holder.itemView.context, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}