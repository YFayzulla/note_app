package com.example.note;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Reference UI elements
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = findViewById(R.id.editTextNoteContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        listViewNotes = findViewById(R.id.listViewNotes);

        // Save button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                refreshNoteList();
            }
        });

        // Update button click listener
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    updateNote();
                    refreshNoteList();
                }
            }
        });

        // Delete button click listener
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNote != null) {
                    deleteNote();
                    refreshNoteList();
                }
            }
        });

        // ListView item click listener
        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = (Note) parent.getItemAtPosition(position);
                editTextNoteTitle.setText(selectedNote.getTitle());
                editTextNoteContent.setText(selectedNote.getContent());
            }
        });

        // Populate ListView on activity startup
        refreshNoteList();
    }

    // Save note to the database
    private void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();
        Note note = new Note(title, content);

        // Check if the note was successfully added
        boolean isAdded = dbHelper.addNote(note);
        if (isAdded) {
            Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show();
        }

        clearFields();
    }

    // Update existing note
    private void updateNote() {
        selectedNote.setTitle(editTextNoteTitle.getText().toString());
        selectedNote.setContent(editTextNoteContent.getText().toString());
        dbHelper.updateNote(selectedNote);
        clearFields();
        selectedNote = null;
    }

    // Delete selected note
    private void deleteNote() {
        dbHelper.deleteNote(selectedNote.getId());
        clearFields();
        selectedNote = null;
    }

    // Refresh ListView to show all notes
    private void refreshNoteList() {
        List<Note> noteList = dbHelper.getAllNotes();
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        listViewNotes.setAdapter(adapter);
    }

    // Clear input fields
    private void clearFields() {
        editTextNoteTitle.setText("");
        editTextNoteContent.setText("");
    }
}
