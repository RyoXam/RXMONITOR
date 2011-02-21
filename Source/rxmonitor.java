// xmonitor.java : implementation file
// Contains the Nirva external service class xmonitor

// Please insert code where there is a TODO string in comments

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetConnection;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcFd;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Tcp;
import org.hyperic.sigar.cmd.Version;

import com.nirvasoft.nirva.nvcmd;



// class instancied when the service is started
class rxmonitor
{
	java.util.Hashtable<String,rxmonitorsession> Sessions;
	
	// Called one time when the service is started
	// Should return true if successful and false otherwise
	public boolean Init()
	{
		// Create a hash table for maintaining session objects
		Sessions = new java.util.Hashtable<String,rxmonitorsession>();
		System.gc();
		// TODO
		// Insert init service code here

		// Everything is OK		
		return true;
	}
	
	
	// Called one time when the service is stopped
	// Should return true if successful and false otherwise
	public boolean Exit()
	{
		// TODO
		// Insert service cleanup code here
		
		// Free the session hash table
		Sessions.clear();
		Sessions = null;
		return true;
	}
	
	
	public boolean Command()
	{
		nvcmd NvCommand = new nvcmd();

		// Used for testing the service
		if(NvCommand.IsCommand("RXMONITOR", "NOP", ""))
			return true;


		// Check the session init command
		String SessionId = NvCommand.GetSessionId();
		if(NvCommand.IsCommand("SYSTEM", "NV_INIT_SESSION", "SERVER"))
		{
			// Create a new session object
			rxmonitorsession Session = new rxmonitorsession(SessionId);
			// Init the object
			boolean Result = true;
			try
			{
				Result = Session.OnInit(NvCommand);
			}
			catch(Throwable e)
			{
				Result = false;
			}
			if(!Result)
				return false;

			// Put the session object into the hash table
			Sessions.put(SessionId, Session);
			return true;	// end processing init session
		}

		// Get the session object from the hash table
		rxmonitorsession Session = Sessions.get(SessionId);
		if(Session == null)
			return false;
		
		// Check the session exit command
		if(NvCommand.IsCommand("SYSTEM", "NV_EXIT_SESSION", "SERVER"))
		{
			// Cleanup the session object
			boolean Result = true;
			try
			{
				Result = Session.OnCleanup(NvCommand);
			}
			catch(Throwable e)
			{
				Result = false;
			}
			if(!Result)
				return false;
			// And remove it from the hash table
			Sessions.remove(SessionId);
			return true;	// end processing exit session
		}
		
		// Other command processing
		boolean Result = true;
		try
		{
			Result = Session.OnCommand(NvCommand);
		}
		catch(Throwable e)
		{
			Result = false;
		}
		if(!Result)
			return false;
		
		return true;
	}
}

class rxmonitorsession
{
	// Session Id
	String sessionId;

	static String colsep = "µN#I§R§V#Aµ";
	static String linesep = "&N%I#R#V%A&";
	static String rowsep =  "%N&I&R%V#A#";
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// Constructor with session if, do not modify
	public rxmonitorsession(String sessionId) {
		this.sessionId = sessionId;
	}

	// Used for sessions hash table, do not modify
	public int hashCode() {
		return sessionId.hashCode();
	}

	// Called one time when initializing the session
	// return true if successful and false otherwise
	public boolean OnInit(nvcmd Command)
	{
		// TODO
		// Insert eventual initialization code here

		// All is OK
		return true;
	}
	
	
	// Called one time when closing the session
	// return true if successful and false otherwise
	public boolean OnCleanup(nvcmd Command)
	{
		// TODO
		// Insert eventual cleanup code here
		
		// All is OK
		return true;
	}


	// Called for each Nirva command to the service
	// This is the command entry point for session
	// return true if successful and false otherwise
	public boolean OnCommand(nvcmd Command)
	{
		// TODO
		// Insert command processing here
		if(Command.IsCommand("SYSTEM", "INFO", "")){
			OutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			Version.printInfo(ps);
			String s = os.toString();
			System.out.println(s);
			String[] lines = s.split("\n");
			
			String regExp = "^([^\\.]+)\\.+(.*)$";
			Pattern pattern = Pattern.compile(regExp);
			Matcher matcher = null;
			Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|INDSTRINGLIST| NAME=|VERSION_INFO| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
			for (int i=0;i<lines.length;i++){
				matcher = pattern.matcher(lines[i]);
				if (matcher.find()){
					Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|VERSION_INFO| KEY=|"+matcher.group(1)+"| VALUE=|"+matcher.group(2)+"| NV_REVERSE_CONTAINER=|YES|");
				}
			}
		}
		
		
		if (Command.IsCommand("SYSTEM", "MEMORY", "")){
			 //cpu info
	        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|INDSTRINGLIST| NAME=|MEMORY| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");     
	        Sigar sigar = new Sigar();
	        try{
		        Mem memory = sigar.getMem();
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|ACTUAL_FREE| VALUE=|"+memory.getActualFree()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|FREE| VALUE=|"+memory.getFree()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|ACTUAL_USED| VALUE=|"+memory.getActualUsed()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|USED| VALUE=|"+memory.getUsed()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|USED_PERCENT| VALUE=|"+memory.getUsedPercent()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|RAM| VALUE=|"+memory.getRam()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|MEMORY| KEY=|TOTAL| VALUE=|"+memory.getTotal()+"| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
        	}
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally{
	        	sigar.close();
	        }
		}
		
		if (Command.IsCommand("SYSTEM", "CPU", "")){
			 //cpu info
	        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|TABLE| NAME=|CPU_INFO| REPLACE=|YES| COLUMNS=|ID;CACHE;CORES/SOCKET;MHZ;CORES;SOCKETS;VENDOR;TOTAL_USER;TOTAL_SYS;TOTAL_IDLE;TOTAL;NICE| NV_REVERSE_CONTAINER=|YES|");     
	        Sigar sigar = new Sigar();
	        try{
	        	CpuInfo[] cpuinfolist = sigar.getCpuInfoList();
	        	Cpu[] cpulist = sigar.getCpuList();
	        	for (int i=0;i<cpuinfolist.length;i++){
	        		CpuInfo cpuinfo= cpuinfolist[i];
	        		Cpu cpu= cpulist[i];
	        		String datas = "";
	        		datas = i+colsep+cpuinfo.getCacheSize()+colsep+cpuinfo.getCoresPerSocket()+colsep+cpuinfo.getModel()+colsep+cpuinfo.getTotalCores()+colsep+cpuinfo.getTotalSockets()+colsep+cpuinfo.getVendor()+colsep+cpu.getUser()+colsep+cpu.getSys()+colsep+cpu.getTotal()+colsep+cpu.getNice();
	        		Command.Command("NV_CMD=|OBJECT:TABLE_ADD_ROWS| NAME=|CPU_INFO| DATA=|"+datas+"| COLSEP=|"+colsep+"| NV_REVERSE_CONTAINER=|YES|");
	        	}
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally{
	        	sigar.close();
	        }
		}
		
		if (Command.IsCommand("SYSTEM", "FILE_SYSTEM", "")){
			 //cpu info
	        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|TABLE| NAME=|FILE_SYSTEM| COLUMNS=|DEV;DIR;TYPE;TYPE_NAME;TOTAL;USED| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");     
	        Sigar sigar = new Sigar();
	        try{
	        	FileSystem[] filesystemlist = sigar.getFileSystemList();
	        	String datas = "";
	        	for (int i=0;i<filesystemlist.length;i++){
	        		FileSystem filesystem= filesystemlist[i];
	        		String dirname = filesystem.getDirName();
	        		String r;
	        		if (filesystem.getType() !=5){
	        			FileSystemUsage fsu = sigar.getFileSystemUsage(dirname);
	        			r = filesystem.getDevName()+colsep+dirname+colsep+filesystem.getType()+colsep+filesystem.getTypeName()+colsep+fsu.getTotal()+colsep+fsu.getFree();
	        		}
	        		else{
	        			r = filesystem.getDevName()+colsep+dirname+colsep+filesystem.getType()+colsep+filesystem.getTypeName();
	        		}
	        		
	        		datas = datas + r+"\n";
	        	}
	        	Command.Command("NV_CMD=|OBJECT:TABLE_ADD_ROWS| NAME=|FILE_SYSTEM| DATA=|"+datas+"| COLSEP=|"+colsep+"| NV_REVERSE_CONTAINER=|YES|");
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally{
	        	sigar.close();
	        }
		}
		
		if (Command.IsCommand("SYSTEM", "PROCESS_LIST", "")){
			 //cpu info
	        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|TABLE| NAME=|PROCESS_LIST| COLUMNS=|PID;NAME;USER;START_TIME;TOTAL;MEMORY;THREAD;HANDLE| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");     
	        Sigar sigar = new Sigar();
	        //Sigar sigarImpl = new Sigar();
	        //maybe use it
	        //SigarProxy sigar = SigarProxyCache.newInstance(sigarImpl, 1000);
	        try{
	        	long[] proclist = sigar.getProcList();
	        	String datas = "";
	        	for (int i=0;i<proclist.length;i++){
	        		long pid= proclist[i];
	        		ProcCpu pcpu = sigar.getProcCpu(pid);
	        		//System.out.println("FVALUE "+pcpu.getPercent());
	        		Thread.sleep(100);
	        		//System.out.println("SVALUE "+pcpu.getPercent());
	        		ProcState pexe = sigar.getProcState(pid);
	        		ProcTime ptime = sigar.getProcTime(pid);
	        		ProcFd procfd= sigar.getProcFd(pid);
	        		ProcMem procmem= sigar.getProcMem(pid);
	        		Date d = new Date(ptime.getStartTime());
	        		String formated_date = dateFormat.format(d);
	        		
	        		String r;
	        		r = pid+colsep+pexe.getName()+colsep+pcpu.getUser()+colsep+formated_date+colsep+CpuPerc.format(pcpu.getPercent())+colsep+procmem.getSize()+colsep+pexe.getThreads()+colsep+procfd.getTotal();
	        		
	        		datas = datas + r+"\n";
	        	}
	        	Command.Command("NV_CMD=|OBJECT:TABLE_ADD_ROWS| NAME=|PROCESS_LIST| DATA=|"+datas+"| COLSEP=|"+colsep+"| NV_REVERSE_CONTAINER=|YES|");
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally{
	        	//SigarProxyCache.clear(sigar);
	        	sigar.close();
	        }
		}
		
		if(Command.IsCommand("SYSTEM", "NETSTAT", "")){
			int LADDR_LEN = 20;
			int RADDR_LEN = 35;
			boolean wantPid = true;
			boolean isNumeric = true;
			Sigar sigar = new Sigar();
		
			try{
				Tcp stat = sigar.getTcp();
		        final String dnt = "    ";
		        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|INDSTRINGLIST| NAME=|CONNECTIONS_SUMMARY| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|ACTIVE_OPEN| VALUE=|"+stat.getActiveOpens()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|PASSIVE_OPEN| VALUE=|"+stat.getPassiveOpens()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|ATTEMPT_FAILS| VALUE=|"+stat.getAttemptFails()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|RESET_RECEIVED| VALUE=|"+stat.getEstabResets()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|ESTABLISHED| VALUE=|"+stat.getCurrEstab()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|SEGMENT_RECEIVED| VALUE=|"+stat.getInSegs()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|SEGMENT_SEND_OUT| VALUE=|"+stat.getOutSegs()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|SEGMENT_RETRANSMITED| VALUE=|"+stat.getRetransSegs()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|BAD_SEGMENT| VALUE=|"+stat.getInErrs()+"| NV_REVERSE_CONTAINER=|YES|");
		        Command.Command("NV_CMD=|OBJECT:INDSTRINGLIST_SET_VALUE| NAME=|CONNECTIONS_SUMMARY| KEY=|RESET_SENT| VALUE=|"+stat.getOutRsts()+"| NV_REVERSE_CONTAINER=|YES|");

		        int flags = NetFlags.CONN_CLIENT | NetFlags.CONN_SERVER | NetFlags.CONN_PROTOCOLS | NetFlags.CONN_RAW | NetFlags.CONN_TCP; 
		        
		        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|TABLE| NAME=|CONNECTIONS| COLUMNS=|TYPE;LOCAL;FOREIGN;STATE;PID| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");
		        
		        NetConnection[] connections = sigar.getNetConnectionList(flags);
		        String datas="";
		        for (int i=0; i<connections.length; i++) {
		            NetConnection conn = connections[i];
		            String proto = conn.getTypeString();
		            String state;

		            if (conn.getType() == NetFlags.CONN_UDP) {
		                state = "";
		            }
		            else {
		                state = conn.getStateString();
		            }

		            String process = null;
		            if (wantPid &&
		                //XXX only works w/ listen ports
		                (conn.getState() == NetFlags.TCP_LISTEN))
		            {
		                try {
		                    long pid =sigar.getProcPort(conn.getType(),conn.getLocalPort());
		                    if (pid != 0) { //XXX another bug
		                        String name =sigar.getProcState(pid).getName();
		                        process = pid + "/" + name;
		                    }
		                } catch (SigarException e) {
		                	//System.out.println("SIGAREXCEPTIOn");
		                }
		            }

		            if (process == null) {
		                process = "";
		            }
		            String res = proto+colsep+formatAddress(sigar,conn.getType(),conn.getLocalAddress(),conn.getLocalPort(),LADDR_LEN,isNumeric)+colsep+formatAddress(sigar,conn.getType(),conn.getRemoteAddress(),conn.getRemotePort(),RADDR_LEN,isNumeric)+colsep+state+colsep+process;
		            //System.out.println(res);
		            datas = datas + res +"\n";
		        }
	            Command.Command("NV_CMD=|OBJECT:TABLE_ADD_ROWS| NAME=|CONNECTIONS| DATA=|"+datas+"| COLSEP=|"+colsep+"| NV_REVERSE_CONTAINER=|YES|");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				sigar.close();
			}
		}
		
		if (Command.IsCommand("SYSTEM", "NET_INTERFACE", "")){
			 //net interface
	        Command.Command("NV_CMD=|OBJECT:CREATE| TYPE=|TABLE| NAME=|NET_INTERFACE| COLUMNS=|NAME;DESCRIPTION;ADDRESS;BROADCAST;NETMASK;HWADRR;METRICS;MTU| REPLACE=|YES| NV_REVERSE_CONTAINER=|YES|");     
	        Sigar sigar = new Sigar();
	        try{
	        	String[] netinterface = sigar.getNetInterfaceList();
	        	String datas="";
	        	for (int i=0;i<netinterface.length;i++){
	        		String interface_name = netinterface[i];
	        		NetInterfaceConfig nic = sigar.getNetInterfaceConfig(interface_name);
	        		String r = nic.getName()+colsep+nic.getDescription()+colsep+nic.getAddress()+colsep+nic.getBroadcast()+colsep+colsep+nic.getDestination()+colsep+nic.getNetmask()+colsep+nic.getHwaddr()+colsep+nic.getMetric()+colsep+nic.getMtu();
	        		datas = datas + r +" \n";
	        	}
	        	Command.Command("NV_CMD=|OBJECT:TABLE_ADD_ROWS| NAME=|NET_INTERFACE| DATA=|"+datas+"| COLSEP=|"+colsep+"| NV_REVERSE_CONTAINER=|YES|");
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        finally{
	        	sigar.close();
	        }
		}
		
		// Everything is OK		
		return true;
	}
		
	private String formatPort(Sigar sigar, int proto, long port, boolean numeric) {
        if (port == 0) {
            return "*";
        }
       if (!numeric) {
            String service = sigar.getNetServicesName(proto, port);
            if (service != null) {
                return service;
            }
        }
        return String.valueOf(port);
    }

    private String formatAddress(Sigar sigar, int proto, String ip,long portnum, int max, boolean numeric) {
        
        String port = formatPort(sigar, proto, portnum,numeric);
        String address;

        if (NetFlags.isAnyAddress(ip)) {
            address = "*";
        }
        else if (numeric) {
            address = ip;
        }
        else {
            try {
                address = InetAddress.getByName(ip).getHostName();
            } catch (UnknownHostException e) {
                address = ip;
            }
        }

        max -= port.length() + 1;
        if (address.length() > max) {
            address = address.substring(0, max);
        }

        return address + ":" + port; 
    }
    
    private String seconds_converter(long seconds){
    	//very hard !!!  
    	seconds = seconds / 1000;
    	long hrs = seconds / 3600;
    	long remainder = seconds % 3600;
    	long min = remainder / 60;
    	long sec= remainder % 60;
    	return hrs+":"+min+":"+sec;
    }
}



