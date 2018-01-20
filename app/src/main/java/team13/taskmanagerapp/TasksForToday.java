package team13.taskmanagerapp;

/**
 * Created by anton on 15.01.2018.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TasksForToday extends Fragment {

    private RecyclerView recyclerView;
    final DataSource dataSource = new DataSource();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Сегодня");
        View rootView = inflater.inflate(R.layout.tasksfortoday, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.container);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ItemViewHolder(
                        getActivity().getLayoutInflater().inflate(R.layout.card, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Item item = dataSource.getItem(position);
                ((ItemViewHolder) holder).bind(item, item.getId());
            }

            @Override
            public int getItemCount() {
                return dataSource.getCount();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rootView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.addItem(generateItem());
            }
        });

        return rootView;
    }

    private Item generateItem() {
        return new Item("Task", "Must be done!");
    }

    private static class Item {

        private final String text1;
        private String text2;
        private Integer id;

        Item(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;
            this.id = 0;
        }

        public String getText1() {
            return text1;
        }

        public String getText2() {
            return text2;
        }
        Integer getId() {
            return id;
        }
    }

    private class DataSource {

        private final List<Item> items = new ArrayList<>();

        public int getCount() {
            return items.size();
        }

        public Item getItem(int position) {
            return items.get(position);
        }

        public void addItem(Item item) {
            item.id = items.size();
            if (items.size() % 3 == 0)
            item.text2 = "Of course!";
            else if (items.size() % 3 == 1)
                item.text2 = "Obviously!";
            items.add(item);
            final int position = items.size() - 1;
            recyclerView.getAdapter().notifyItemInserted(position);
            recyclerView.scrollToPosition(position);
        }

        void removeTask(int id) {
            for (int ind = 0; ind < items.size(); ind++) {
                if (items.get(ind).getId().equals(id)) {
                    items.remove(ind);
                    recyclerView.getAdapter().notifyItemRemoved(ind);
                    break;
                }
            }
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView text1;
        private final TextView text2;
        private Button dlttsk;
        private Button edttsk;
        private int id;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.text1 = (TextView) itemView.findViewById(R.id.text1);
            this.text2 = (TextView) itemView.findViewById(R.id.text2);
            dlttsk = itemView.findViewById(R.id.dlttsk);
            edttsk = itemView.findViewById(R.id.edttsk);
        }

        void bind(Item item, final int id) {
            text1.setText(item.getText1());
            text2.setText(item.getText2());
            this.id = id;
            dlttsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataSource.removeTask(id);
                }
            });
        }

    }

}