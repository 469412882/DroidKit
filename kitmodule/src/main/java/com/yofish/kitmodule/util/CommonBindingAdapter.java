package com.yofish.kitmodule.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.jakewharton.rxbinding.view.RxView;
import com.yofish.imagemodule.ImgLoader;
import com.yofish.imagemodule.ImgLoaderListener;
import com.yofish.imagemodule.ImgOptions;
import com.yofish.kitmodule.R;
import com.yofish.kitmodule.baseAdapter.abslistview.DBLvAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.CommonAdapter;
import com.yofish.kitmodule.baseAdapter.recyclerview.DBRvAdapter;
import com.yofish.kitmodule.binding.command.BindingCommand;
import com.yofish.kitmodule.wedget.refresh.RefreshContainer;
import com.yofish.kitmodule.wedget.viewpager.DBPagerAdapter;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 常用的bindingAdapter
 * <p>
 * Created by hch on 2018/12/13.
 */
public class CommonBindingAdapter {

    //防重复点击间隔(秒)
    public static final long CLICK_INTERVAL = 1000;

    public CommonBindingAdapter(){}

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView var0, Bitmap var1) {
        var0.setImageBitmap(var1);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(ImageView var0, int var1) {
        var0.setImageResource(var1);
    }


    @BindingAdapter({"bindBitmapImg"})
    public static void loadBitmapImg(ImageView imageView, Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        new ImgLoader.Builder().loadType(ImgOptions.LoadType.BITMAP).bitmap(bitmap).into(imageView);
    }

    /**
     * View 点击事件
     * @param view
     * @param clickCommand
     * @param isThrottleFirst 是否开启防连击
     * @param interval 间隔时间 默认1000毫秒
     */
    @BindingAdapter(value = {"onClickCommand", "isThrottleFirst","intervalTimes"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand clickCommand, final boolean isThrottleFirst, final long interval) {
        if (isThrottleFirst) {
            RxView.clicks(view)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        } else {
            long intervalTime = CLICK_INTERVAL;
            try {
                intervalTime = interval <= 0 ? CLICK_INTERVAL : interval;
            } catch (Exception e) {
                intervalTime = CLICK_INTERVAL;
            }
            RxView.clicks(view)
                    .throttleFirst(intervalTime, TimeUnit.MILLISECONDS)//1秒钟内只允许点击1次
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        }
    }

    /**
     * View 长按事件
     * @param view
     * @param clickCommand
     */
    @BindingAdapter(value = {"onLongClickCommand"}, requireAll = false)
    public static void onLongClickCommand(View view, final BindingCommand clickCommand) {
        RxView.longClicks(view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (clickCommand != null) {
                            clickCommand.execute();
                        }
                    }
                });
    }

    /**
     * 回调控件本身
     * @param currentView
     * @param bindingCommand
     */
    @BindingAdapter(value = {"currentView"}, requireAll = false)
    public static void replyCurrentView(View currentView, BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            bindingCommand.execute(currentView);
        }
    }

    /**
     * View 是否需要焦点
     * @param view
     * @param needRequestFocus
     */
    @BindingAdapter({"requestFocus"})
    public static void requestFocusCommand(View view, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    /**
     * View焦点发生变化的事件绑定
     * @param view
     * @param onFocusChangeCommand
     */
    @BindingAdapter({"onFocusChangeCommand"})
    public static void onFocusChangeCommand(View view, final BindingCommand<Boolean> onFocusChangeCommand) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeCommand != null) {
                    onFocusChangeCommand.execute(hasFocus);
                }
            }
        });
    }

    /**
     * 图片加载 带有监听
     * imgShape=0原图 imgShape=1圆形图 imgShape=2圆角图
     * @param imageView
     * @param url
     * @param shape
     * @param startCommand
     * @param finishCommand
     * @param failedCommand
     * @param progressCommand
     */
    @BindingAdapter(value = {"bindNetImg", "imgShape", "loadStartCommand","loadFinishCommand","loadFailedCommand","loadProgressCommand"}, requireAll = false)
    public static void loadNetImg(ImageView imageView, String url, int shape, final BindingCommand startCommand,
                                  final BindingCommand finishCommand, final BindingCommand failedCommand,
                                  final BindingCommand<ImgLoaderListenerWrapper> progressCommand) {
        if (TextUtils.isEmpty(url)) {
            return;
        } else {
            int holder;
            ImgLoader.Builder builder = new ImgLoader.Builder();
            if (0 == shape) {
                holder = R.drawable.netimg_default_rect_shape;
                builder.url(url).placeHolder(holder).error(holder);
            } else if (1 == shape) {
                holder = R.drawable.netimg_default_round_shape;
                builder.imgType(ImgOptions.ImgType.CIRCLE).url(url).placeHolder(holder).error(holder);
            } else if (2 == shape) {
                holder = R.drawable.netimg_default_fillet_shape;
                builder.imgType(ImgOptions.ImgType.ROUND).url(url).placeHolder(holder).error(holder);
            }

            if (startCommand == null && finishCommand == null && failedCommand == null && progressCommand == null) {
                builder.into(imageView);
            }else {
                builder.listener(new ImgLoaderListener() {
                    @Override
                    public void start() {
                        if (startCommand != null) {
                            startCommand.execute();
                        }
                    }

                    @Override
                    public void finish() {
                        if (finishCommand != null) {
                            finishCommand.execute();
                        }
                    }

                    @Override
                    public void progress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                        if (progressCommand != null) {
                            progressCommand.execute(new ImgLoaderListenerWrapper(isComplete,percentage,bytesRead,totalBytes));
                        }
                    }

                    @Override
                    public void failed() {
                        if (failedCommand != null) {
                            failedCommand.execute();
                        }
                    }
                }).intoWithListener(imageView);
            }
        }
    }

    /**
     * 刷新监听
     * @param refreshContainer
     * @param onRefreshCommand
     */
    @BindingAdapter(value = {"onRefreshCommand","noAutoRefresh"}, requireAll = false)
    public static void onRefresh(RefreshContainer refreshContainer, final BindingCommand onRefreshCommand,boolean noAutoRefresh) {
        refreshContainer.setAutoRefreshing();
        refreshContainer.setOnRefreshListener(new RefreshContainer.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }
        });
    }

    /**
     * 加载更多监听
     * @param refreshContainer
     * @param onLoadMoreCommand
     */
    @BindingAdapter(value = {"onLoadMoreCommand"}, requireAll = false)
    public static void onLoadMore(RefreshContainer refreshContainer, final BindingCommand onLoadMoreCommand) {
        refreshContainer.setOnLoadMoreListener(new RefreshContainer.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
            }
        });
    }

    /**
     * CheckBox绑定监听
     * @param checkBox
     * @param bindingCommand
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bindingCommand.execute(b);
            }
        });
    }

    /**
     * RadioGroup绑定监听
     * @param radioGroup
     * @param bindingCommand
     */
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                bindingCommand.execute(radioButton.getText().toString());
            }
        });
    }

    /**
     * EditText重新获取焦点的事件绑定
     * @param editText
     * @param needRequestFocus
     */
    @BindingAdapter(value = {"requestFocus"}, requireAll = false)
    public static void requestFocusCommand(EditText editText, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
        editText.setFocusableInTouchMode(needRequestFocus);
    }

    /**
     * EditText输入文字改变的监听
     * @param editText
     * @param textChanged
     */
    @BindingAdapter(value = {"textChanged"}, requireAll = false)
    public static void addTextChangedListener(EditText editText, final BindingCommand<String> textChanged) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (textChanged != null) {
                    textChanged.execute(text.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * listview滑动监听
     * @param listView
     * @param onScrollChangeCommand
     * @param onScrollStateChangedCommand
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final ListView listView,
                                             final BindingCommand<ListViewScrollDataWrapper> onScrollChangeCommand,
                                             final BindingCommand<Integer> onScrollStateChangedCommand) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
                if (onScrollStateChangedCommand != null) {
                    onScrollStateChangedCommand.execute(scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ListViewScrollDataWrapper(scrollState, firstVisibleItem, visibleItemCount, totalItemCount));
                }
            }
        });

    }

    /**
     * listview item点击监听
     * @param listView
     * @param onItemClickCommand
     */
    @BindingAdapter(value = {"onItemClickCommand"}, requireAll = false)
    public static void onItemClickCommand(final ListView listView, final BindingCommand<Integer> onItemClickCommand) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickCommand != null) {
                    onItemClickCommand.execute(position);
                }
            }
        });
    }

    /**
     * RecyclerView 滑动监听
     * @param recyclerView
     * @param onScrollChangeCommand
     * @param onScrollStateChangedCommand
     */
    @BindingAdapter(value = {"onScrollChangeCommand", "onScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final RecyclerView recyclerView,
                                             final BindingCommand<ScrollDataWrapper> onScrollChangeCommand,
                                             final BindingCommand<Integer> onScrollStateChangedCommand) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int state;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ScrollDataWrapper(dx, dy, state));
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (onScrollStateChangedCommand != null) {
                    onScrollStateChangedCommand.execute(newState);
                }
            }
        });

    }

    /**
     * NestedScrollView 滑动监听
     * @param nestedScrollView
     * @param onScrollChangeCommand
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter({"onScrollChangeCommand"})
    public static void onScrollChangeCommand(final NestedScrollView nestedScrollView, final BindingCommand<NestScrollDataWrapper> onScrollChangeCommand) {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new NestScrollDataWrapper(scrollX, scrollY, oldScrollX, oldScrollY));
                }
            }
        });
    }

    /**
     * ScrollView 滑动监听
     * @param scrollView
     * @param onScrollChangeCommand
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter({"onScrollChangeCommand"})
    public static void onScrollChangeCommand(final ScrollView scrollView, final BindingCommand<ScrollVDataWrapper> onScrollChangeCommand) {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (onScrollChangeCommand != null) {
                    onScrollChangeCommand.execute(new ScrollVDataWrapper(scrollView.getScaleX(), scrollView.getScrollY()));
                }
            }
        });

    }

    /**
     * viewPager 滑动监听
     * @param viewPager
     * @param onPageScrolledCommand
     * @param onPageSelectedCommand
     * @param onPageScrollStateChangedCommand
     */
    @BindingAdapter(value = {"onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"}, requireAll = false)
    public static void onScrollChangeCommand(final ViewPager viewPager,
                                             final BindingCommand<ViewPagerDataWrapper> onPageScrolledCommand,
                                             final BindingCommand<Integer> onPageSelectedCommand,
                                             final BindingCommand<Integer> onPageScrollStateChangedCommand) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int state;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageScrolledCommand != null) {
                    onPageScrolledCommand.execute(new ViewPagerDataWrapper(position, positionOffset, positionOffsetPixels, state));
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedCommand != null) {
                    onPageSelectedCommand.execute(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                this.state = state;
                if (onPageScrollStateChangedCommand != null) {
                    onPageScrollStateChangedCommand.execute(state);
                }
            }
        });

    }

    /**
     * RecyclerView 数据绑定adapter
     * @param recyclerView
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, DBRvAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView 非数据绑定adapter
     * @param recyclerView
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, CommonAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * ListView 数据绑定adapter
     * @param listView
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setListAdapter(ListView listView, DBLvAdapter adapter) {
        listView.setAdapter(adapter);
    }

    /**
     * GridView 数据绑定adapter
     * @param gridView
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setGridAdapter(GridView gridView, DBLvAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    /**
     * ListView 非数据绑定adapter
     * @param listView
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setListViewAdapter(ListView listView, com.yofish.kitmodule.baseAdapter.abslistview.CommonAdapter adapter) {
        listView.setAdapter(adapter);
    }

    /**
     * viewPager 数据绑定adapter
     * @param viewPager
     * @param adapter
     */
    @BindingAdapter({"bindAdapter"})
    public static void setViewPagerAdapter(ViewPager viewPager, DBPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter({"bindOnLongClick"})
    public static void onLongClick(View view, View.OnLongClickListener longClickListener) {
        view.setOnLongClickListener(longClickListener);
    }

    @BindingAdapter({"errhint"})
    public static void onErrHint(TextInputLayout textInputLayout, String str) {
        textInputLayout.setError(str);
    }

    @BindingAdapter({"onMenuClickListener"})
    public static void onMenuClickListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }


    /**
     * 图片加载进度Wrapper
     */
    public static class ImgLoaderListenerWrapper {
        public boolean isComplete;
        public int percentage;
        public long bytesRead;
        public long totalBytes;

        public ImgLoaderListenerWrapper(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
            this.isComplete = isComplete;
            this.percentage = percentage;
            this.bytesRead = bytesRead;
            this.totalBytes = totalBytes;
        }
    }

    /**
     * ListView Wrapper
     */
    public static class ListViewScrollDataWrapper {
        public int firstVisibleItem;
        public int visibleItemCount;
        public int totalItemCount;
        public int scrollState;

        public ListViewScrollDataWrapper(int scrollState, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.firstVisibleItem = firstVisibleItem;
            this.visibleItemCount = visibleItemCount;
            this.totalItemCount = totalItemCount;
            this.scrollState = scrollState;
        }
    }

    /**
     * RecyclerView Wrapper
     */
    public static class ScrollDataWrapper {
        public float scrollX;
        public float scrollY;
        public int state;

        public ScrollDataWrapper(float scrollX, float scrollY, int state) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.state = state;
        }
    }

    /**
     * ScrollView Wrapper
     */
    public static class ScrollVDataWrapper {
        public float scrollX;
        public float scrollY;

        public ScrollVDataWrapper(float scrollX, float scrollY) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
        }
    }

    /**
     * NestScrollView Wrapper
     */
    public static class NestScrollDataWrapper {
        public int scrollX;
        public int scrollY;
        public int oldScrollX;
        public int oldScrollY;

        public NestScrollDataWrapper(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            this.scrollX = scrollX;
            this.scrollY = scrollY;
            this.oldScrollX = oldScrollX;
            this.oldScrollY = oldScrollY;
        }
    }

    /**
     * ViewPager Wrapper
     */
    public static class ViewPagerDataWrapper {
        public float positionOffset;
        public float position;
        public int positionOffsetPixels;
        public int state;

        public ViewPagerDataWrapper(float position, float positionOffset, int positionOffsetPixels, int state) {
            this.positionOffset = positionOffset;
            this.position = position;
            this.positionOffsetPixels = positionOffsetPixels;
            this.state = state;
        }
    }
}
