package com.fahim.newapp.ui.front;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fahim.newapp.Interface.AdapterClickListener;
import com.fahim.newapp.R;
import com.fahim.newapp.adapter.BookListViewAdapter;
import com.fahim.newapp.holder.BookHolder;
import com.fahim.newapp.network.APICall;
import com.fahim.newapp.ui.books.BooksViewModel;
import com.fahim.newapp.utils.Preferences;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class BookListViewFragment extends Fragment implements AdapterClickListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private BookListViewAdapter bookAdapter;
    private List<BookHolder> bookHolderList = new ArrayList<BookHolder>();
    BooksViewModel booksViewModel;
    SwipeRefreshLayout.OnRefreshListener refreshListener;
    Preferences preferences = new Preferences();
    SweetAlertDialog sweetAlertDialog;

    ProgressBar progressBar;
    DownloadZipFileTask downloadZipFileTask;
    private File sdcard = Environment.getExternalStorageDirectory();
    private String remainingPath;


    public BookListViewFragment() {
        // Required empty public constructor
    }

    public static BookListViewFragment newInstance() {
        return new BookListViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        booksViewModel =
                ViewModelProviders.of(this).get(BooksViewModel.class);
        // Inflate the layout for this fragment
        booksViewModel.setContext(getActivity());
        View view = inflater.inflate(R.layout.book_list_view, container, false);
        recyclerView = view.findViewById(R.id.book_list);
        swipeRefreshLayout = view.findViewById(R.id.book_list_refresh);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        bookAdapter = new BookListViewAdapter(getActivity(), bookHolderList, this);
        recyclerView.setAdapter(bookAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readBookData(false);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readBookData(true);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

    }

    @Override
    public void onClick(int id) {

        switch (id) {
            case R.id.download:
                fileDownload(preferences.getSelectedBookLink(getActivity()));
                break;
            case R.id.add_to_fav:
                break;
            case R.id.book_name:
                openWithoutDownload();
                break;
        }

    }

    private void openWithoutDownload() {
        File file = new File(sdcard, File.separator + getString(R.string.app_name) + File.separator + File.separator + preferences.getSelectedBookName(getActivity()) + ".pdf");
        if (file.exists()) {
            Uri mpath = Uri.fromFile(file);
            Log.e("create pdf uri path==>", "" + mpath);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(mpath, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent.createChooser(intent, "open with"));

            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(),
                        "There is no any PDF Viewer",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (preferences.getSelectedBookLink(getActivity()).startsWith("https://drive")) {
            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getSelectedBookLink(getActivity())));
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Please install PDF app", Toast.LENGTH_SHORT).show();
            }

        } else if (preferences.getSelectedBookLink(getActivity()).endsWith("pdf")) {
            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getSelectedBookLink(getActivity())));
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Please install PDF app", Toast.LENGTH_SHORT).show();
            }

        } else if (preferences.getSelectedBookLink(getActivity()).endsWith("zip")) {
            Toast.makeText(getActivity(), R.string.cannot_open_zip_file, Toast.LENGTH_LONG).show();
        }

    }

    private void fileDownload(String selectedBookLink) {
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        sweetAlertDialog.setTitleText(getActivity().getString(R.string.t_downloading));
        URL url = null;
        try {
            url = new URL(selectedBookLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        String base = url.getProtocol() + "://" + url.getHost();
        remainingPath = selectedBookLink.replace(base, "");
        Log.e("TAG", selectedBookLink);
        Log.e("TAG", path);
        Log.e("TAG", base);
        Log.e("remainingPath", remainingPath);

        String foldername = getString(R.string.app_name);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + foldername);
        directory.mkdirs();

        File downloadedFile = new File(directory + File.separator + preferences.getSelectedBookName(getActivity()) + ".pdf");

        Log.e("T", downloadedFile.toString());
        if (url.getHost().toLowerCase().contains("drive")) {

            Intent target = new Intent(Intent.ACTION_VIEW, Uri.parse(preferences.getSelectedBookLink(getActivity())));
            try {
                startActivity(target);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "Please install PDF app", Toast.LENGTH_SHORT).show();
            }


        } else if (downloadedFile.exists()) {

            Uri mpath = Uri.fromFile(downloadedFile);
            Log.e("create pdf uri path==>", "" + mpath);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(mpath, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent.createChooser(intent, "open with"));

            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(),
                        "There is no any PDF Viewer",
                        Toast.LENGTH_SHORT).show();
            }
        } else {


            APICall downloadService = createService(APICall.class, base);
            Call<ResponseBody> call = downloadService.downloadFileByUrl(remainingPath);

            call.enqueue(new Callback<ResponseBody>() {


                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.d("onResponse", String.valueOf(call.request().url()));
                    if (response.isSuccessful()) {
                        Log.d("TAG", "Got the body for the file");

                        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_SHORT).show();

                        downloadZipFileTask = new DownloadZipFileTask();
                        downloadZipFileTask.execute(response.body());

                    } else {
                        Log.d("TAG", "Connection failed ");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Log.e("TAG", t.getMessage());
                    Log.d("onFailure", String.valueOf(call.request().url()));

                }
            });
        }
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(new OkHttpClient.Builder().build())
                .build();
        return retrofit.create(serviceClass);
    }


    private void readBookData(final boolean callApi) {
        booksViewModel.getBookHolder(callApi).observe(getViewLifecycleOwner(), new Observer<List<BookHolder>>() {
            @Override
            public void onChanged(List<BookHolder> holders) {
                Log.e("onChanged", "BookHolder" + holders.size());
                if (holders == null || holders.size() == 0) {

                    if (callApi == false) {
                        swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(true);
                                // directly call onRefresh() method
                                refreshListener.onRefresh();
                            }
                        });
                    }
                } else {

                    bookAdapter = new BookListViewAdapter(getActivity(), holders, BookListViewFragment.this);
                    recyclerView.setAdapter(bookAdapter);
                    bookAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });
    }


    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sweetAlertDialog.show();
        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call

            if (getFileExtension().equalsIgnoreCase("pdf")) {
                writeResponseBodyToDisk(urls[0]);
            } else {
                saveToDisk(urls[0]);

            }
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.e("API123", progress[0].second + " ");

            if (progress[0].first == 100)
//                Toast.makeText(getApplicationContext(), getString(R.string.s_file_downloaded_sucess), Toast.LENGTH_SHORT).show();


                if (progress[0].second > 0) {
                    final int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sweetAlertDialog.setTitleText(getString(R.string.t_downloading) + currentProgress);
                        }
                    });
                }

            if (progress[0].first == -1) {
                Toast.makeText(getActivity(), getString(R.string.s_download_failed), Toast.LENGTH_SHORT).show();
            }

        }

        void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }


        @Override
        protected void onPostExecute(String result) {

            sweetAlertDialog
                    .setTitleText(getActivity().getString(R.string.w_success))
                    .setConfirmText(getActivity().getString(R.string.w_ok))
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            bookAdapter.notifyDataSetChanged();

        }
    }

    private String getFileExtension() {
        if (remainingPath.lastIndexOf(".") != -1 && remainingPath.lastIndexOf(".") != 0)
            return remainingPath.substring(remainingPath.lastIndexOf(".") + 1);
        else return "";
    }

    private void saveToDisk(ResponseBody body) {
        try {

            File destinationFile = new File(sdcard, File.separator + getString(R.string.app_name) + File.separator + File.separator + preferences.getSelectedBookName(getActivity()) + ".zip");

            new ReadPDF(destinationFile).execute("");

            InputStream inputStream = null;
            OutputStream outputStream1 = null;

            try {

                inputStream = body.byteStream();
                outputStream1 = new FileOutputStream(destinationFile);
                byte[] data = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                while ((count = inputStream.read(data)) != -1) {
                    outputStream1.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                }

                outputStream1.flush();
//                new ReadERPStock(destinationFile).execute("");


                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, (long) -1);
                downloadZipFileTask.doProgress(pairs);
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream1 != null) outputStream1.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReadPDF extends AsyncTask<String, Void, String> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private File file;
        private boolean isloaded;

        public ReadPDF(File file) {
            this.file = file;

        }

        @Override
        protected String doInBackground(String... params) {

            // getJSONWebService();
            isloaded = unpackZisp(file);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            progressSpotsDialog.dismiss();
            //  if(isloaded)
            //  new ReadByteStock().execute("");

//            new ReadJsonStockFile().execute("");
        }

        @Override
        protected void onPreExecute() {
            // spotAlertDialogDataFromCloud.show();
//            progressSpotsDialog.show();
        }
    }


    private boolean unpackZisp(File file) {
        InputStream is;
        ZipInputStream zis;
        try {
            is = new FileInputStream(file);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            File f1 = new File(sdcard, File.separator + getString(R.string.app_name) + File.separator + File.separator + preferences.getSelectedBookName(getActivity()) + ".pdf");

            if (f1.exists()) {
                f1.delete();
            }
            while ((ze = zis.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int count;

                String filename = ze.getName();


                FileOutputStream fout = new FileOutputStream(f1);

                // reading and writing
                //byte[] bytes =null;
                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    fout.write(bytes);
                    baos.reset();

                }


                buffer = null;
                baos = null;
                fout.close();
                zis.closeEntry();
            }

            zis.close();

            Log.e("@@zip finished", "");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File destinationFile = new File(sdcard, File.separator + getString(R.string.app_name) + File.separator + File.separator + preferences.getSelectedBookName(getActivity()) + ".pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
