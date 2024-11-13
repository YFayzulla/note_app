package com.example.note;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editTextNoteTitle, editTextNoteContent;
    private Button buttonSave, buttonUpdate, buttonDelete;
    private ListView listViewNotes;
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewNotes = findViewById(R.id.listViewNotes);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                refreshNoteList();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    updateNote();
                    refreshNoteList();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    deleteNote();
                    refreshNoteList();
                }
            }
        });

        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = (Note) parent.getItemAtPosition(position);
                editTextNoteTitle.setText(selectedNote.getTitle());
                editTextNoteContent.setText(selectedNote.getContent());
            }
        });

        refreshNoteList();
    }

    private void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();
        Note note = new Note(title, content);
        dbHelper.addNote(note);
        clearFields();
    }

    private void updateNote() {
        selectedNote.setTitle(editTextNoteTitle.getText().toString());
        selectedNote.setContent(editTextNoteContent.getText().toString());
        dbHelper.updateNote(selectedNote);
        clearFields();
        selectedNote = null;
    }

    private void deleteNote() {
        dbHelper.deleteNote(selectedNote.getId());
        clearFields();
        selectedNote = null;
    }

    private void refreshNoteList() {
        List<Note> noteList = dbHelper.getAllNotes();
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        listViewNotes.setAdapter(adapter);
    }

    private void clearFields() {
        editTextNoteTitle.setText("");
        editTextNoteContent.setText("");
    }
}
