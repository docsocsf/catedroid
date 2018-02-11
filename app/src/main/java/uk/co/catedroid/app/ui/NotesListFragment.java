package uk.co.catedroid.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import uk.co.catedroid.app.R;
import uk.co.catedroid.app.data.model.Note;
import uk.co.catedroid.app.viewmodel.NotesListViewModel;

public class NotesListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, rootView);

        NotesListViewModel viewModel = ViewModelProviders.of(this).get(NotesListViewModel.class);

        viewModel.getNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                Log.d("CATe", "NotesListFragment/notes changed");
                for (Note n: notes) {
                    Log.d("CATe", "  " + n.toString());
                }
            }
        });

        return rootView;
    }
}
