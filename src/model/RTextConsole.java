package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.rosuda.*;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

public class RTextConsole implements RMainLoopCallbacks{

	@Override
	public void rBusy(Rengine re, int which) {
		System.out.println("rBusy("+which+")");
	}

	@Override
	public String rChooseFile(Rengine arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rFlushConsole(Rengine arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rLoadHistory(Rengine arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String rReadConsole(Rengine re, String prompt, int addToHistory) {
		 System.out.print(prompt);
	        try {
	            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	            String s=br.readLine();
	            return (s==null||s.length()==0)?s:s+"\n";
	        } catch (Exception e) {
	            System.out.println("jriReadConsole exception: "+e.getMessage());
	        }
	        return null;
	}

	@Override
	public void rSaveHistory(Rengine arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rShowMessage(Rengine re, String message) {
		System.out.println("rShowMessage \""+message+"\"");
		
	}

	@Override
	public void rWriteConsole(Rengine re, String text, int oType) {
		System.out.println(text);
		
	}

}
