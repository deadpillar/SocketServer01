import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// sampleMultiChat 의 소켓
public class MyServer {
    public static ArrayList<PrintWriter> m_OutputList;

    static class ClientManagerThread extends Thread{

        private Socket m_socket;
        private String m_ID;

        @Override
        public void run(){
            super.run();
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
                String text;

                while(true){
                    text = in.readLine();
                    if(text!=null) {
                        for(int i=0;i<MyServer.m_OutputList.size();++i){
                            MyServer.m_OutputList.get(i).println(text);
                            MyServer.m_OutputList.get(i).flush();
                        }
                    }
                }

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        public void setSocket(Socket _socket){
            m_socket = _socket;
        }
    }

    public static void main(String[] args){
        m_OutputList = new ArrayList<PrintWriter>();

        try{
            ServerSocket s_socket = new ServerSocket(3000);
            while(true){
                Socket c_socket = s_socket.accept();
                ClientManagerThread c_thread = new ClientManagerThread();
                c_thread.setSocket(c_socket);

                m_OutputList.add(new PrintWriter(c_socket.getOutputStream()));
                System.out.println(m_OutputList.size());
                c_thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
