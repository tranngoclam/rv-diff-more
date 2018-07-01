package io.github.tranngoclam.rvdiffmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.github.tranngoclam.rvdiffmore.databinding.ItemUserBinding;
import io.reactivex.functions.Consumer;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements
    Consumer<Pair<List<User>, DiffUtil.DiffResult>> {

  private final OnUserClickListener onUserClickListener;

  private List<User> users;

  UserAdapter(OnUserClickListener onUserClickListener) {
    this.users = new ArrayList<>();
    this.onUserClickListener = onUserClickListener;
  }

  @Override
  public void accept(Pair<List<User>, DiffUtil.DiffResult> pair) {
    if (pair.first == null) {
      return;
    }

    if (pair.second == null) {
      addUsers(pair.first);
    } else {
      this.users = pair.first;
      pair.second.dispatchUpdatesTo(this);
    }
  }

  @Override
  public int getItemCount() {
    return this.users.size();
  }

  @Override
  public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
    holder.bind(this.users.get(position));
  }

  @NonNull
  @Override
  public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false), onUserClickListener);
  }

  public void addUsers(List<User> users) {
    int size = this.users.size();
    this.users.addAll(users);
    notifyItemRangeInserted(size, users.size());
  }

  public List<User> getUsers() {
    return this.users;
  }

  static class UserItemCallback extends SimpleDiffUtilCallbacks<User> {

    static UserItemCallback create(List<User> oldItems, List<User> newItems) {
      return new UserItemCallback(oldItems, newItems);
    }

    private UserItemCallback(List<User> oldItems, List<User> newItems) {
      super(oldItems, newItems);
    }

    @Override
    public boolean areItemsTheSame(User oldItem, User newItem) {
      return oldItem.getId().equals(newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
      return oldItem.equals(newItem);
    }
  }

  static class UserViewHolder extends RecyclerView.ViewHolder {

    private final ItemUserBinding binding;

    UserViewHolder(@NonNull View itemView, OnUserClickListener onUserClickListener) {
      super(itemView);
      binding = DataBindingUtil.bind(itemView);
      if (binding != null && onUserClickListener != null) {
        binding.setItemClick(onUserClickListener);
      }
    }

    void bind(User user) {
      binding.setUser(user);
    }
  }
}
