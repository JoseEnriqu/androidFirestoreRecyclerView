package com.desoft.lastrayopedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.desoft.lastrayopedidos.interfaces.CategoryOnclickInterface;
import com.desoft.lastrayopedidos.objetos.CatTienda;
import com.desoft.lastrayopedidos.objetos.tienda;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class productosActivity extends AppCompatActivity {



    //Recycler view sobre el que se listarán todos los items
    private RecyclerView recyclerView;

    //administrador de layout: Que definirá la distribucion de los item(cantidad de items por linea, 1 2 3)
    RecyclerView.LayoutManager layoutManager;

    //Conexíon a la base de datos
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //Adaptador del recycler
    private FirestoreRecyclerAdapter adapter;


    String tienda;
    TextView tituloTienda;
    String idTienda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Intent intent = getIntent();
        tienda = intent.getStringExtra("tienda");
        idTienda = intent.getStringExtra("idTienda");
        tituloTienda = (TextView) findViewById(R.id.txtCat);
        if(!tienda.isEmpty()){
            tituloTienda.setText(tienda);
        }

        //Referencia al recycler de la actividad
        recyclerView = (RecyclerView) findViewById(R.id.recycCategoria);

        //Consulta a la base de datos, primero el nombre de la colleccion que quieres listar, WHEREEQUALTO("")
        Query query = db.collection("categoriaProducto").whereEqualTo("empresa", idTienda);

        FirestoreRecyclerOptions<CatTienda> options = new FirestoreRecyclerOptions.Builder<CatTienda>()
                .setQuery(query, CatTienda.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<CatTienda, catProdViewHolder>(options) {

            @NonNull
            @Override
            public catProdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias_producto, parent, false);
                return new catProdViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull catProdViewHolder holder, int position, @NonNull final CatTienda model) {
                holder.catProdTitulo.setText(model.getCategoriaProducto());
                final String documentId = getSnapshots().getSnapshot(position).getId();
                holder.CategoryInterfaceClick(new CategoryOnclickInterface() {
                    @Override
                    public void onClick(View view, boolean isLongPressed) {
                        Intent intent = new Intent( productosActivity.this, productosShow.class);
                        intent.putExtra("categoriaProd", documentId);
                        intent.putExtra("tienda", idTienda);
                        intent.putExtra("nombreTienda", model.getCategoriaProducto());
                        startActivity(intent);
                    }
                });

            }
        };
        layoutManager = new GridLayoutManager(this, 1 );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    private static class catProdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView catProdTitulo;
        CategoryOnclickInterface categoryOnclickInterface;

        public catProdViewHolder(@NonNull View itemView) {
            super(itemView);
            catProdTitulo  = (TextView) itemView.findViewById(R.id.txtCatProd);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryOnclickInterface.onClick(view, false);
        }

        public void CategoryInterfaceClick(CategoryOnclickInterface categoryOnclickInterface){
            this.categoryOnclickInterface = categoryOnclickInterface;
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}