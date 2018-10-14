package com.yason.doubanmovie.common.net.download;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.yason.doubanmovie.common.config.Config;
import com.yason.doubanmovie.common.net.callback.IRequest;
import com.yason.doubanmovie.common.net.callback.ISuccess;
import com.yason.doubanmovie.common.util.file.FileUtil;
import java.io.File;
import java.io.InputStream;

/**
 * @author Yason
 * @since 2018/1/30
 */

public class SaveFileTask extends AsyncTask<Object, Void, File> {

  private final ISuccess SUCCESS;
  private final IRequest REQUEST;

  public SaveFileTask(ISuccess success, IRequest request) {
    SUCCESS = success;
    REQUEST = request;
  }

  @Override
  protected File doInBackground(Object... objects) {
    final InputStream is = (InputStream) objects[0];
    String downloadDir = (String) objects[1];
    String name = (String) objects[2];
    String extension = (String) objects[3];

    if (downloadDir == null || downloadDir.equals("")) {
      downloadDir = "down_loads";
    }

    if (extension == null || extension.equals("")) {
      extension = "";
    }

    if (name == null || name.equals("")) {
      return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
    } else {
      return FileUtil.writeToDisk(is, downloadDir, name);
    }

  }

  @Override
  protected void onPostExecute(File file) {
    if (SUCCESS != null) {
      SUCCESS.onSuccess(file.getPath());
    }
    if (REQUEST != null) {
      REQUEST.onRequestEnd();
    }
    autoInstallApk(file);
  }

  private void autoInstallApk(File file) {
    if (FileUtil.getExtension(file.getPath()).equals("apk")) {
      final Intent install = new Intent();
      install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      install.setAction(Intent.ACTION_VIEW);
      install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
      Config.getApplicationContext().startActivity(install);
    }
  }

}
