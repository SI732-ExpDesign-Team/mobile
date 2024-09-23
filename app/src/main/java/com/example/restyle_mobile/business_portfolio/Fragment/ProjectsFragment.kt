package Fragment

import Adapter.ProjectAdapter
import Persistence.OpenHelper
import Persistence.UserHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restyle_mobile.R

class ProjectsFragment : Fragment() {
    lateinit var dbHelper: OpenHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.projects_info, container, false).apply {
            dbHelper = OpenHelper(requireContext())
            findViewById<RecyclerView>(R.id.rvProjects)?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ProjectAdapter(dbHelper.getProjects())
            }
        }
    }
}
