package uk.co.catedroid.app.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.ButterKnife
import uk.co.catedroid.app.R
import uk.co.catedroid.app.data.model.Note
import uk.co.catedroid.app.viewmodel.NotesListViewModel

class NotesListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_notes, container, false)
        ButterKnife.bind(this, rootView)

        val viewModel = ViewModelProviders.of(this).get(NotesListViewModel::class.java)

        viewModel.notes!!.observe(this, Observer { notes ->
            Log.d("CATe", "NotesListFragment/notes changed")
            for (n in notes!!) {
                Log.d("CATe", "  " + n.toString())
            }
        })

        return rootView
    }
}
