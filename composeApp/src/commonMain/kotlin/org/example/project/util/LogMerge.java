package com.starline.basic_knowledge.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class LogMerge {

    // remove log line such as "DisplayManager"
    private static boolean filterSystemInsertLogLine = true;

    private static String rootPath = "/Users/max/Desktop/20240827191758 VER-AN10 套餐开启状态，控制中心移动数据开关未置灰";
    private static String mergeFilePath = rootPath + "/merged-log.log";
    private static String mergeOnlyPackageFilePath = rootPath + "/merged-only-package-log.log";

    private static ArrayList<File> gzFiles = new ArrayList<>();
    private static ArrayList<File> logFiles = new ArrayList<>();

    private static ArrayList<Integer> processId = new ArrayList<>();


    public static void main(String[] args) {
        File file = new File(rootPath);

        // find all gz file
        if (file.isDirectory() && file.listFiles() != null) {
            for (int i = 0; i < file.listFiles().length; i++) {
                File childFile = file.listFiles()[i];
                if (childFile.getName().contains("applogcat-log.") && childFile.getName().contains(".gz")) {
                    gzFiles.add(childFile);
                }
            }
        }

        // sort by index
        gzFiles.sort((o1, o2) -> {
            int o1Index = getIndexOfFile(o1.getName());
            int o2Index = getIndexOfFile(o2.getName());
            return Integer.compare(o1Index, o2Index);
        });

        // unzip all gz file
        System.out.println("unzip file count:[" + gzFiles.size() + "]");
        for (int i = 0; i < gzFiles.size(); i++) {
            System.out.println("gzFile:[" + gzFiles.get(i).getName() + "]");
            unzip(gzFiles.get(i).getPath());
        }

        // merge unzip log file
        merge(logFiles);

        // delete temp unzip file
        for (File logFile : logFiles) {
            logFile.delete();
        }

        // filter only red tea package
        findProcessId(mergeFilePath);
        System.out.println("ProcessId = " + processId);
        filterOnlyRedTeaProcessId();
        System.out.println("job finished");
    }

    private static void unzip(String gzFilePath) {
        String unzipPath = gzFilePath.replace(".gz", "");
        try {
            FileInputStream fis = new FileInputStream(gzFilePath);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(unzipPath);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            gis.close();
            fis.close();

            logFiles.add(new File(unzipPath));
            System.out.println("File decompressed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void merge(ArrayList<File> logFiles) {
        try {
            FileOutputStream fos = new FileOutputStream(mergeFilePath);

            for (File logFile : logFiles) {
                FileInputStream fis = new FileInputStream(logFile);
                byte[] buffer = new byte[2048];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fis.close();
                logFile.delete()
            }

            fos.close();
            System.out.println("Files concatenated successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findProcessId(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("beginning of")) {
                    String tag = getTagByLogLine(line);
                    int id = getProcessIdByLogLine(line);
                    if (tag.contains("RTRoaming") || tag.contains("RedTea")) {
                        if (!processId.contains(id)) {
                            System.out.println("process id find in: " + line);
                            processId.add(id);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getIndexOfFile(String fileName) {
        fileName = fileName.replace("applogcat-log.", "");
        fileName = fileName.split("\\.")[0].replace("I", "");
        return Integer.parseInt(fileName);
    }

    private static void filterOnlyRedTeaProcessId() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mergeFilePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(mergeOnlyPackageFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("beginning of")) {
                    int id = getProcessIdByLogLine(line);
                    for (int i = 0; i < processId.size(); i++) {
                        if (processId.contains(id)) {
                            if (filterSystemInsertLogLine) {
                                if (line.contains("performTraversals: cancel draw reason")
                                        || line.contains("DisplayManager")
                                        || line.contains("W System")
                                        || line.contains("VRI[")
                                        || line.contains("InputTransport")
                                        || line.contains("InputMethodManager")
                                        || line.contains("SurfaceControl")
                                        || line.contains("InputEventReceiver")
                                        || line.contains("ClientTransactionHandler")
                                        || line.contains("BufferQueueProducer")
                                        || line.contains("InsetsSourceConsumer")
                                        || line.contains("ImeFocusController")
                                        || line.contains("IPCThreadState")
                                        || line.contains("ResourcesCompat")
                                        || line.contains("AconfigFlags")
                                ) {
                                    break;
                                }
                            }
                            writer.write(line);
                            writer.newLine();
                            break;
                        }
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("文件操作出现错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getTagByLogLine(String line) {
        line = line.replaceAll("\\s+", " ");
        String tag = "";
        try {
            tag = line.split(" ")[5];
        } catch (Exception e) {
            System.out.println("get log tag wrong line [" + line + "]");
        }
        return tag;
    }

    private static int getProcessIdByLogLine(String line) {
        line = line.replaceAll("\\s+", " ");
        int processId = 0;
        try {
            String process = line.split(" ")[2];
            processId = Integer.parseInt(process);
        } catch (Exception e) {
            System.out.println("get process id wrong line [" + line + "]");
        }
        return processId;
    }

}
