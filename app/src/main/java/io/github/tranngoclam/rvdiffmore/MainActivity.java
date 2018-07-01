package io.github.tranngoclam.rvdiffmore;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;

public class MainActivity extends AppCompatActivity {

  private static final int PER_PAGE = 10;

  private static final String TAG = MainActivity.class.getSimpleName();

  private static final int VISIBLE_THRESHOLD = 1;

  @Inject UserService userService;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();

  private int lastVisibleItem, totalItemCount;

  private RecyclerView list;

  private boolean loading = false;

  private int pageId = 1;

  private PublishProcessor<Integer> pageSubject = PublishProcessor.create();

  private SwipeRefreshLayout swipe;

  private UserAdapter userAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ((App) getApplication()).getAppComponent().inject(this);

    userAdapter = new UserAdapter(user -> Toast.makeText(this, user.getName().toString(), Toast.LENGTH_SHORT).show());

    list = findViewById(R.id.list);
    swipe = findViewById(R.id.swipe);

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    list.setHasFixedSize(true);
    list.setLayoutManager(layoutManager);
    list.setItemAnimator(new DefaultItemAnimator());
    list.setAdapter(userAdapter);
    list.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = layoutManager.getItemCount();
        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        if (!loading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
          pageId++;
          pageSubject.onNext(pageId);
          loading = true;
        }
      }
    });

    swipe.setOnRefreshListener(() -> {
      if (loading) {
        return;
      }
      initialize();
    });

    initialize();
  }

  @Override
  protected void onDestroy() {
    if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
      compositeDisposable.clear();
    }
    super.onDestroy();
  }

  private void initialize() {
    pageId = 1;
    pageSubject = PublishProcessor.create();

    Pair<List<User>, DiffUtil.DiffResult> initialPair = Pair.create(userAdapter.getUsers(), null);

    Disposable disposable = pageSubject
        .onBackpressureDrop()
        .filter(integer -> !loading)
        .doOnNext(integer -> loading = true)
        .concatMap(integer -> userService.getUsers(pageId, PER_PAGE))
        .map(Response::getData)
        .scan(initialPair, (pair, next) -> {
          UserAdapter.UserItemCallback diffCallbacks = UserAdapter.UserItemCallback.create(pair.first, next);
          DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallbacks, true);
          return Pair.create(next, pageId == 1 ? result : null);
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(pair -> {
          loading = false;
          swipe.setRefreshing(false);
          userAdapter.accept(pair);
          if (pageId == 1) {
            list.smoothScrollToPosition(0);
          }
        }, throwable -> {
          loading = false;
          swipe.setRefreshing(false);
          Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }, () -> {
          loading = false;
          swipe.setRefreshing(false);
        });

    compositeDisposable.add(disposable);

    loadMore();
  }

  private void loadMore() {
    pageSubject.onNext(pageId);
  }
}
