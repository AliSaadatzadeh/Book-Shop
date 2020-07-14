package ir.skynic.bookshop.fragments;

import android.app.Fragment;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ir.skynic.bookshop.Configuration;
import ir.skynic.bookshop.R;
import ir.skynic.bookshop.api.ApiClient;
import ir.skynic.bookshop.model.Comment;
import ir.skynic.bookshop.view.CommentView;

public class CommentsFragment extends Fragment {

    private int bookId;

    private View mView;

    private CardView btnSend;
    private ViewGroup commentContainer;
    private EditText edtComment;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_comments, null);
        mView.findViewById(R.id.btnBack).setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });

        initUi();
        getComments();
        return mView;

    }

    private void sendComment() {
        String comment = edtComment.getText().toString();
        ApiClient.executeCommand(new String[]{"add-comment", Configuration.getUsername(getActivity()), String.valueOf(bookId), comment},
                o -> {
            if(o != null && ((int)o[0]) == 0) {
                Toast.makeText(getActivity(), "نظر شما ثبت شد.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "خطایی رخ داده است. لطفا دوباره سعی کنید.", Toast.LENGTH_SHORT).show();
            }
            edtComment.setText("");
        });
    }

    private void getComments() {
        String request[] = {"get-comment", Configuration.getUsername(getActivity()), String.valueOf(bookId), "999999999999"};
        ApiClient.getModel(request, "comment", Comment.class, o -> {
            if(o != null) {
                commentContainer.removeAllViews();

                List<Comment> commentList = (List) o[1];
                for (Comment comment : commentList) {
                    CommentView commentView = new CommentView(getActivity(), comment);
                    commentContainer.addView(commentView);
                }
            } else {
                Toast.makeText(getActivity(), "خطایی پیش آمد... لطفا دوباره امتحان کنید.", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        });

    }

    private void initUi() {
        mView.findViewById(R.id.imgSend).setOnClickListener(view -> sendComment());
        commentContainer = mView.findViewById(R.id.lnrCommentContainer);
        edtComment = mView.findViewById(R.id.edtComment);
        progressBar = mView.findViewById(R.id.progressBar);
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
