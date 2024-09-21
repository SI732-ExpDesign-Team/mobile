package Adapter

import Beans.Projects
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restyle_mobile.ProjectsViewHolder
import com.example.restyle_mobile.R

class ProjectAdapter(val projects:List<Projects>):RecyclerView.Adapter<ProjectsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        return ProjectsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.render(projects[position])
    }

    override fun getItemCount(): Int {
        return projects.size
    }
}