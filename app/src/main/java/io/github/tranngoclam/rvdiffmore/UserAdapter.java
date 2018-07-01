package io.github.tranngoclam.rvdiffmore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.github.tranngoclam.rvdiffmore.databinding.ItemUserBinding;
import io.reactivex.functions.Consumer;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
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
      addItems(pair.first);
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
  public int getItemViewType(int position) {
    User user = getItemAt(position);
    if (user != null && user.getId() == null) {
      return R.layout.item_progress;
    }
    return R.layout.item_user;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    User user = getItemAt(position);
    if (holder instanceof ProgressViewHolder) {
      ((ProgressViewHolder) holder).bind();
    } else if (holder instanceof UserViewHolder) {
      ((UserViewHolder) holder).bind(user);
    }
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    if (viewType == R.layout.item_progress) {
      return new ProgressViewHolder(inflater.inflate(R.layout.item_progress, parent, false));
    }
    return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false), onUserClickListener);
  }

  List<User> getItems() {
    return this.users;
  }

  void hideProgress() {
    int lastPosition = getItemCount() - 1;
    if (0 <= lastPosition) {
      User lastUser = getItemAt(lastPosition);
      if (lastUser != null && lastUser.getId() == null) {
        remoteItem(lastPosition);
      }
    }
  }

  void showProgress() {
    addItem(new User());
  }

  private void addItem(@Nullable User user) {
    this.users.add(user);
    notifyItemInserted(getItemCount() - 1);
  }

  private void addItems(List<User> users) {
    int size = this.users.size();
    this.users.addAll(users);
    notifyItemRangeInserted(size, users.size());
  }

  @Nullable
  private User getItemAt(int position) {
    if (position < 0 || position > getItemCount() - 1) {
      return null;
    }

    return this.users.get(position);
  }

  private void remoteItem(int position) {
    this.users.remove(position);
    notifyItemRemoved(getItemCount());
  }

  static class ProgressViewHolder extends RecyclerView.ViewHolder {

    ProgressViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    void bind() {

    }
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
      if (oldItem.getId() == null || newItem.getId() == null) {
        return true;
      }
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
