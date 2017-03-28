package com.datazuul.apps.mediaplayer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JProgressBar;

public class SeekBar extends JProgressBar {

  private int updatedValue = 0; //sharing between different scopes

  /**
   * Update SeekBar position
   *
   * @param progress in microseconds
   * @param totalVal in seconds
   */
  public void updateSeekBar(long progress, float totalVal) {
    BackgroundExecutor.get().execute(new UpdatingTask(progress, totalVal)); //Another thread will calculate the relative position
    setValue(updatedValue);
  }

  /**
   * Task used for updating the seek value in another thread.
   *
   * @author Pierluigi
   */
  private class UpdatingTask implements Runnable {

    long progress;
    float totalVal;

    public UpdatingTask(long progress, float totalVal) {
      this.progress = progress;
      this.totalVal = totalVal;
    }

    @Override
    public void run() {
      int lp = (int) (progress / 1000); //progress comes in microseconds
      int seekLenght = getMaximum();
      int n = (int) ((lp / (totalVal * 1000)) * seekLenght);
//      updatedValue = lastSeekVal + n;
      updatedValue = n;
    }
  }
  ///////////////////////////////////////////////////////////

  /**
   * New Constructor, sets a mouseListener (extends JProgressBar)
   */
  public SeekBar() {
    super();
    setMaximum(10000); //it's smoother this way
    addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        float val = ((float) e.getX() / getWidth()) * getMaximum();
        returnValueToPlayer(val);
        setValue((int) val);
        log("SeekBar pressed: " + val + " x: " + e.getX());

      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }
    });
  }

  /**
   * Informs the player about the relative value selected in the seekbar
   *
   * @throws BasicPlayerException
   */
  private void returnValueToPlayer(float val) {
    //TODO inform our player
  }

  private void log(String str) {
    System.out.println("SeekBar] " + str);
  }
}
