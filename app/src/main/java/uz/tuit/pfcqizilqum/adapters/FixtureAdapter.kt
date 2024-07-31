package uz.jameschamberlain.footballteamtracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.tuit.pcfqizilqum.Utils
import com.tuit.pcfqizilqum.databinding.ItemFixtureBinding
import com.tuit.pcfqizilqum.fixtures.FixturesFragment
import com.tuit.pcfqizilqum.fixtures.FixturesFragmentDirections
import com.tuit.pcfqizilqum.data.Fixture
import com.tuit.pcfqizilqum.data.FixtureResult
import com.tuit.pcfqizilqum.viewmodels.FixturesViewModel

class FixtureAdapter(
        options: FirestoreRecyclerOptions<Fixture>,
        private val context: Context,
        private val parentFragment: FixturesFragment,
        private val viewModel: FixturesViewModel
) : FirestoreRecyclerAdapter<Fixture, FixtureAdapter.FixtureHolder>(options) {

    override fun onDataChanged() {
        if (itemCount == 0)
            parentFragment.addNoFixturesLayout()
        else
            parentFragment.removeNoFixturesLayout()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureHolder {
        val itemBinding = ItemFixtureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FixtureHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FixtureHolder, position: Int, model: Fixture) {
        // Update the result with the actual score
        model.score = model.score

        holder.dateTextView.text = model.dateString()
        holder.timeTextView.text = model.timeString()
        holder.resultTextView.text = model.result.text
        holder.resultTextView.setTextColor(FixtureResult.getColor(model.result, context))
        holder.scoreTextView.text = model.score.toString()

        holder.homeTeamTextView.text = if (model.isHomeGame) Utils.getTeamName(parentFragment.requireActivity()) else model.opponent
        holder.awayTeamTextView.text = if (model.isHomeGame) model.opponent else Utils.getTeamName(parentFragment.requireActivity())

        holder.parentLayout.setOnClickListener {
            viewModel.selectFixture(model)
            val action = FixturesFragmentDirections.actionFixturesFragmentToFixtureDetailsFragment(
                    fixtureId = this.snapshots.getSnapshot(position).id
            )
            NavHostFragment
                    .findNavController(parentFragment)
                    .navigate(action)
        }
    }

    class FixtureHolder(itemBinding: ItemFixtureBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var dateTextView: TextView = itemBinding.dateTextView
        var timeTextView: TextView = itemBinding.timeTextView
        var resultTextView: TextView = itemBinding.resultTextView
        var homeTeamTextView: TextView = itemBinding.homeTeamTextView
        var scoreTextView: TextView = itemBinding.scoreTextView
        var awayTeamTextView: TextView = itemBinding.awayTeamTextView
        var parentLayout: ConstraintLayout = itemBinding.root
    }


}