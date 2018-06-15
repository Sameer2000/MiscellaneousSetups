package com.app.src;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestThreadPrinting
{

   // WAP to print 010203040506
   public static void main( String[] args )
   {
      MyTRunnable runnable = new MyTRunnable();
      Thread t1 = new Thread( runnable, "zero" );
      Thread t2 = new Thread( runnable, "even" );
      Thread t3 = new Thread( runnable, "odd" );
      t1.start();
      t2.start();
      t3.start();
   }

}

class MyTRunnable implements Runnable
{

   int number = 0;
   private static boolean zero = true, even, odd;

//   Object monitor = new Object();
   Lock lock = new ReentrantLock();
   Condition condition = lock.newCondition();
   @Override
   public void run()
   {
      print();
      System.out.println( "End print" );
   }

   private void print()
   {
      try
      {
         while ( true )
         {
            if ( number == 6 )
            {
               break;
            }
            lock.lock();
            
               if ( Thread.currentThread().getName().equals( "zero" ) )
               {
                  if ( !zero )
                  {
                     condition.await();
                  }
                  else
                  {
                     System.out.print( 0 );
                     zero = false;
                     odd = !even;
                     even = !odd;
                     condition.signalAll();
                  }
               }

               if ( Thread.currentThread().getName().equals( "odd" ) )
               {
                  if ( !odd )
                  {
                     condition.await();
                  }
                  else
                  {
                     System.out.print( ++number );
                     zero = true;
                     odd = false;
                     
                     /** it should be fixed here   **/
                     even = true;
                     
                     
                     condition.signalAll();
                  }
               }

               if ( Thread.currentThread().getName().equals( "even" ) )
               {
                  if ( !even )
                  {
                     condition.await();
                  }
                  else
                  {
                     System.out.print( ++number );
                     zero = true;
                     odd = false;
                     even = false;
                     condition.signalAll();
                  }

               }
            }
         lock.unlock();
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }

}