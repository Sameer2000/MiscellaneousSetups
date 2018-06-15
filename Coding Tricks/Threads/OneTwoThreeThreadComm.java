package com.test.module;

public class OneTwoThreeThreadComm
{

   public static void main( String[] args )
   {

      PrintNumber number = new PrintNumber();
      Thread1 t1 = new Thread1( number );
      Thread2 t2 = new Thread2( number );
      Thread3 t3 = new Thread3( number );
      t1.start();
      t2.start();
      t3.start();
   }

}

class Thread1 extends Thread
{

   PrintNumber pr;

   public Thread1( PrintNumber pr )
   {
      this.setName( "Thread-1" );
      this.pr = pr;
   }

   @Override
   public void run()
   {
      pr.print1();
   }

}

class Thread2 extends Thread
{
   PrintNumber pr;

   public Thread2( PrintNumber pr )
   {
      this.setName( "Thread-2" );
      this.pr = pr;
   }

   @Override
   public void run()
   {
      pr.print2();
   }

}

class Thread3 extends Thread
{

   PrintNumber pr;

   public Thread3( PrintNumber pr )
   {
      this.setName( "Thread-3" );
      this.pr = pr;
   }

   @Override
   public void run()
   {
      pr.print3();
   }

}

class PrintNumber
{

   public boolean onePrinted;
   public boolean twoPrinted;
   public boolean threePrinted;

   synchronized public void print1()
   {
      while ( true )
      {
         try
         {
            if ( !onePrinted && !twoPrinted && !threePrinted )
            {
               Thread.sleep( 500 );
               System.out.println( "Thread : " + Thread.currentThread().getName() + " - " + 1 );
               onePrinted = true;
               notifyAll();
            }
            else
            {
               wait();
            }
         }
         catch ( InterruptedException e )
         {
            e.printStackTrace();
         }
      }
   }

   synchronized public void print2()
   {
      while ( true )
      {
         try
         {
            if ( onePrinted && !twoPrinted && !threePrinted )
            {
               Thread.sleep( 500 );
               System.out.println( "Thread : " + Thread.currentThread().getName() + " - " + 2 );
               twoPrinted = true;
               notifyAll();
            }
            else
            {
               wait();
            }
            Thread.sleep( 500 );
         }
         catch ( Exception e )
         {

         }
      }
   }

   synchronized public void print3()
   {
      while ( true )
      {
         try
         {
            if ( onePrinted && twoPrinted && !threePrinted )
            {
               Thread.sleep( 500 );
               System.out.println( "Thread : " + Thread.currentThread().getName() + " - " + 3 );
               onePrinted = false;
               twoPrinted = false;
               notifyAll();
            }
            else
            {
               wait();
            }
         }
         catch ( Exception e )
         {

         }
      }
   }

}