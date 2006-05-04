package service;
/*
SerWebsiteContentLoader.java by Geist Alexander 

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import model.BOSettingsProxy;

import java.util.logging.Logger;

import control.ControlMain;

public abstract class SerWebsiteContentLoader {

    public static String getWebsiteContent(String website, int port, String pfadAndparameters) {
        StringBuffer htmlContent = new StringBuffer();
        try {
            String tmpUrl = website + ":" + port + pfadAndparameters;
            BOSettingsProxy proxySettings = ControlMain.getSettingsProxy();
            if (proxySettings.isUse()) {
                System.getProperties().put("proxyHost", proxySettings.getHost());
                System.getProperties().put("proxyPort", proxySettings.getPort());
            }
            URL url = new URL(tmpUrl);
            URLConnection uc = url.openConnection();
            if (proxySettings.isUse()) {
                uc.setRequestProperty("Proxy-Authorization",
                                      "Basic " + proxySettings.getUserPass());
            }
            InputStream content = uc.getInputStream();
            BufferedReader in =
                new BufferedReader(new InputStreamReader(content));
            String line;
            while ( (line = in.readLine()) != null) {
                htmlContent.append(line);
            }
            if (proxySettings.isUse()) {
                System.getProperties().remove("proxyHost");
                System.getProperties().remove("proxyPort");
            }
        }
        catch (IOException e) {
            Logger.getLogger("SerWebsiteContentLoader").warning(e.getMessage()); 
        }
        catch (Exception e) {
            Logger.getLogger("SerWebsiteContentLoader").warning(e.getMessage());
        }
        return htmlContent.toString();
    }
}
