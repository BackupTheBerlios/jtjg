package control.muxxer;
/*
ControlMuxxerView.java by Geist Alexander 

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  

*/ 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import control.ControlMain;

import model.BOAfterRecordOptions;
import model.BOExternalProcess;
import presentation.muxxer.GuiMuxxerView;
import service.SerExternalProcessHandler;
import service.SerInputStreamListener;
import service.SerProcessStopListener;

public class ControlMuxxerView implements ActionListener, SerProcessStopListener, SerInputStreamListener {

	GuiMuxxerView view;
	BOAfterRecordOptions options;
    SerProcessStopListener muxStopListener;
    ArrayList files;
    int mplexProgressMax=0;
    
    boolean projectXStarted = false;
    boolean mplexStarted = false;
    BOExternalProcess pxProcess;
    BOExternalProcess mplexProcess;
    
    File mplexOutputFile;
    
    /*
     * Konstruktor für die Bearbeitung nach der Aufnahme
     */
    public ControlMuxxerView(BOAfterRecordOptions options, SerProcessStopListener listener, ArrayList files) {
        this.setMuxStopListener(listener);
        this.setOptions(options);
        this.setFiles(files);
        this.setView(new GuiMuxxerView(this));
        this.getView().getPbOk().setVisible(false);
        this.initialize();
        
        view.setVisible(true);
        this.actionOk();
    }

    /*
     * Konstruktor für den Standalone-Mux-Betrieg
     */
	public ControlMuxxerView(BOAfterRecordOptions options, ArrayList files) {
        this.setOptions(options);
        this.setFiles(files);
		this.setView(new GuiMuxxerView(this));
		this.initialize();
		
		view.setVisible(true);
	}
    
    /*
     * Konstruktor für den Settings-Dialog
     */
    public ControlMuxxerView(BOAfterRecordOptions options) {
        this.setOptions(options);
        this.setView(new GuiMuxxerView(this));
        this.initialize();
        
        view.setVisible(true);
    }
    
	private void initialize() {
        switch (this.getOptions().getMplexOption()) {
            case 3: this.getView().getRbMPEG().setSelected(true);
            break;
            case 4: this.getView().getRbSVCD().setSelected(true);
            break;
            case 9: this.getView().getRbDVD().setSelected(true);
        };
        this.getView().getCbStartPX().setSelected(this.getOptions().isUseProjectX());
        this.getView().getCbStartMplex().setSelected(this.getOptions().isUseMplex());
	}
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("ok")) {
		    this.actionOk();
		}
        if (action.equals("Mpeg")) {
            this.getOptions().setMplexOption(3);
        }
        if (action.equals("SVCD")) {
            this.getOptions().setMplexOption(4);
        }
        if (action.equals("DVD")) {
            this.getOptions().setMplexOption(9);
        }
        if (action.equals("cbStartPX")) {
            this.getOptions().setUseProjectX(((JCheckBox)e.getSource()).isSelected());
        }
		if (action.equals("cbStartMplex")) {
            boolean selected = ((JCheckBox)e.getSource()).isSelected();
            this.getView().checkMplexButtons(selected);
            this.getOptions().setUseMplex(selected);
		}
	}
    
    private void actionOk() {
        if (this.getFiles()==null || this.getFiles().size()<0) {
            this.getView().dispose();
        } else {
            this.startMuxxing();
        }    
    }
    
    private void startMuxxing() {
        //PX selected
        if(this.getView().getCbStartPX().isSelected()) {
            this.startProjectX(); 
        } 
        //mplex selected
        else if (this.getView().getCbStartMplex().isSelected()) {
            this.startMplex(this.getFiles()); 
        } 
        //nothing selected
        else {
            this.getView().dispose();
        }
    }
    
    private void startMplex(ArrayList files) {
        mplexOutputFile=null;
        String[] param = new String[7 + files.size()];
        param[0] = ControlMain.getSettingsPath().getMplexPath();
        param[1] = "-v";
        param[2] = "1";
        param[3] = "-f";
        param[4] = Integer.toString(this.getOptions().getMplexOption());
        param[5] = "-o";
        param[6] = this.getMplexOutputFileName((File)files.get(0));
        for (int i = 0; i < files.size(); i++) {
            param[i + 7] = ((File) files.get(i)).getAbsolutePath();
        }
        mplexProcess=SerExternalProcessHandler.startProcess(this, "mplex", param, true);
        this.mplexStarted=true;
        this.handleMplexProgress(files);
    }
    
    private void handleMplexProgress(ArrayList muxxFiles) {
        for (int i=0; i<muxxFiles.size(); i++) {
            long fileLength=((File)muxxFiles.get(i)).length()/1000;
            mplexProgressMax=mplexProgressMax+(int)fileLength;
        }
        this.getView().getProgressMplex().setMaximum(mplexProgressMax);
        
        Thread loopThread = new Thread() {
            public void run() {
                while (mplexStarted) {
                    if (mplexOutputFile.exists()) {
                        long outSize=mplexOutputFile.length()/1000;
                        getView().getProgressMplex().setValue((int)outSize);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            } 
        };
        loopThread.start();
    }
    
    private String getMplexOutputFileName(File file) {
        mplexOutputFile = new File(file.getAbsolutePath()+".mpeg");
        return mplexOutputFile.getAbsolutePath();
    }
    
    private void startProjectX() {
        this.getView().getProgressPX().setValue(0);
        ArrayList params=new ArrayList();
        String[] param = new String[3 + files.size()];
        String separator = System.getProperty("file.separator");

        param[0] = System.getProperty("java.home") + separator + "bin" + separator + "java";
        params.add(System.getProperty("java.home") + separator + "bin" + separator + "java");
        param[1] = "-jar";
        params.add("-jar");
        param[2] = ControlMain.getSettingsPath().getProjectXPath();
        params.add(ControlMain.getSettingsPath().getProjectXPath());
        
        for (int i = 0; i < files.size(); i++) {
            param[i + 3] = ((File) files.get(i)).getAbsolutePath();
            params.add(((File) files.get(i)).getAbsolutePath());
        }
        pxProcess=SerExternalProcessHandler.startProcess(this, this, "ProjectX", param, true);
    }
    
    public void getInputStream (String inputLine) {
        if (!this.projectXStarted) {
            if (inputLine.indexOf("<<< Session")>=0) {
                projectXStarted=true;
            }    
        } else {
            if (inputLine.indexOf("%")==1) {
                this.getView().getProgressPX().setValue(Integer.parseInt(inputLine.substring(0, 1)));
            }
            else if (inputLine.indexOf("%")==2) {
                this.getView().getProgressPX().setValue(Integer.parseInt(inputLine.substring(0, 2)));
                
            }
            else if (inputLine.indexOf("%")==3) {
                this.getView().getProgressPX().setValue(Integer.parseInt(inputLine.substring(0, 3)));
                
            }
        }
    }
    
    /*
     * Wenn ProjectX-process finished, check for using mplex
     * Close dialog when muxxing with mplex is finished
     */
    public void processStopped(int exitCode, String processName) {
        if (processName.equals("ProjectX")) {
            projectXStarted=false;
            if (this.getView().getCbStartMplex().isSelected()) {
//              mplex starten mit demuxten Files     
                this.startMplex(this.getFilesForMplex());
            } else {
                this.getView().dispose();
            }
        }
        if (processName.equals("mplex")) {
            this.mplexStarted=false;
            if (this.getMuxStopListener()!=null) {
                this.getMuxStopListener().processStopped(exitCode, "muxxi");
            }
            this.getView().getProgressMplex().setValue(mplexProgressMax);
            this.getView().dispose();
        }
    }
    
    /*
     * Auswertung des ProjectX-Logs nach demuxten Dateien
     */
    private ArrayList getFilesForMplex() {
        String filename = ((File)this.getFiles().get(0)).getAbsolutePath();
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
        File logFile = new File(nameWithoutExtension+"_X.log");
        ArrayList pxFiles = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(logFile));
            String line;
            while ((line=in.readLine())!=null) {
                if (line.indexOf("---> neue Datei: ")>=0) {
                    pxFiles.add(new File(line.substring(17)));
                }
            }
        } catch (IOException e) {}
        return pxFiles;
    }

    public boolean isMuxing() {
        return this.mplexStarted || this.projectXStarted;
    }
    
    public void stopProjectXProcess() {
        if (this.projectXStarted) {
            this.pxProcess.getProcess().destroy();
        }
    }
    
    public void stopMplexProcess() {
        if (this.mplexStarted) {
            this.mplexProcess.getProcess().destroy();
        }
    }
    
    public void stopAllProcesses() {
        this.stopProjectXProcess();
        this.stopMplexProcess();
    }
    
	/**
	 * @return Returns the options.
	 */
	public BOAfterRecordOptions getOptions() {
		return options;
	}
	/**
	 * @param options The options to set.
	 */
	public void setOptions(BOAfterRecordOptions options) {
		this.options = options;
	}
    /**
     * @return Returns the files.
     */
    public ArrayList getFiles() {
        return files;
    }
    /**
     * @param files The files to set.
     */
    public void setFiles(ArrayList files) {
        this.files = files;
    }
    /**
     * @return Returns the view.
     */
    public GuiMuxxerView getView() {
        return view;
    }
    /**
     * @param view The view to set.
     */
    public void setView(GuiMuxxerView view) {
        this.view = view;
    }
    /**
     * @return Returns the muxStopListener.
     */
    public SerProcessStopListener getMuxStopListener() {
        return muxStopListener;
    }
    /**
     * @param muxStopListener The muxStopListener to set.
     */
    public void setMuxStopListener(SerProcessStopListener recControl) {
        this.muxStopListener = recControl;
    }
}