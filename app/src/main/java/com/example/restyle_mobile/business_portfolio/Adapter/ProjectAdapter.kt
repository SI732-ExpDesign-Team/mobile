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
import com.example.restyle_mobile.business_portfolio.Repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.AlertDialog

class ProjectAdapter(private val projects: MutableList<Projects>) : RecyclerView.Adapter<ProjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = projects[position]
        holder.render(project)

        val deleteIcon = holder.itemView.findViewById<ImageView>(R.id.delete_icon)
        deleteIcon.setOnClickListener {
            // Crear el diálogo de confirmación
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Eliminar Proyecto")
                setMessage("¿Estás seguro de que deseas eliminar este proyecto?")

                setPositiveButton("Sí") { _, _ ->
                    // Confirmación de eliminación
                    GlobalScope.launch(Dispatchers.IO) {
                        val dbHelper = OpenHelper(holder.itemView.context)
                        dbHelper.deleteProject(project.id) // Eliminar de la base de datos

                        withContext(Dispatchers.Main) {
                            ProjectRepository.deleteProject(project.id) // Eliminar del repositorio
                            projects.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, itemCount)
                            Toast.makeText(holder.itemView.context, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                setNegativeButton("No") { dialog, _ ->
                    // Cancelar eliminación
                    dialog.dismiss()
                }

                create().show()
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}
