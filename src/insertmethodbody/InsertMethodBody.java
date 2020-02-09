package insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;


public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	   while (true) {
	   UtilMenu.showMenuOptions();
	   int option = UtilMenu.getOption();
	   switch (option) {
	   case 1:
		   System.out.println("Enter the three input: (e.g, ComponentApp,foo,1 or, ServiceApp,bar,2)");
		   String[] input = UtilMenu.getArguments();
		   try {
		         ClassPool pool = ClassPool.getDefault();
		         pool.insertClassPath(INPUT_DIR);
		         CtClass cc = pool.get("target." + input[0]);
		         cc.defrost();
		         CtMethod m = cc.getDeclaredMethod(input[1]);
		         String block1 = "{ " + _L_ //
		               + "System.out.println(\"[Inserted] "+ "target." + input[0]+"."+ input[1]+"'s"+": \" + $" + input[2]+"); " + _L_+ //
		               "}";
		         System.out.println(block1);
		         m.insertBefore(block1);
		         cc.writeFile(OUTPUT_DIR);
		         System.out.println("[DBG] write output to: " + OUTPUT_DIR);
		         System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
		         
		         ClassPool cp = ClassPool.getDefault();
		         Loader cl = new Loader(cp);
		         Class<?> c = cl.loadClass(cc.getName());
		         String[] var = null;
		         c.getDeclaredMethod("main", new Class[] {String[].class}).
		         		invoke(null, new Object[] { var });
		      } catch (NotFoundException | CannotCompileException | IOException e) {
		         e.printStackTrace();
		      }
		   default:
			   break;
	   }
      
   }
   }
}
