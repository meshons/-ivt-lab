package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  @BeforeEach
  public void init() {
    primaryTorpedoStore = mock(TorpedoStore.class);
    secondaryTorpedoStore = mock(TorpedoStore.class);

    this.ship = new GT4500(primaryTorpedoStore, secondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success() {
    // Arrange

    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verifyNoInteractions(secondaryTorpedoStore);
    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(primaryTorpedoStore).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success() {
    // Arrange

    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(primaryTorpedoStore).fire(1);
    verify(secondaryTorpedoStore, never()).getTorpedoCount();
    verify(secondaryTorpedoStore, times(1)).isEmpty();
    verify(secondaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Twice_Success_bothStore() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean resultPrimary = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, resultPrimary);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, times(1)).fire(1);
    verifyNoInteractions(secondaryTorpedoStore);

    boolean resultSecondary = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(true, resultSecondary);

    verify(secondaryTorpedoStore, never()).getTorpedoCount();
    verify(secondaryTorpedoStore, times(1)).isEmpty();
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Twice_Success_secondaryStoreEmpty() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(primaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean resultPrimary = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, resultPrimary);

    resultPrimary = ship.fireTorpedo(FiringMode.SINGLE);
    assertEquals(true, resultPrimary);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(2)).isEmpty();
    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void fireTorpedo_Single_Failure_TorpedoStore_fire_fail() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(false);

    // Act
    boolean resultPrimary = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, resultPrimary);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, times(1)).fire(1);
    verifyNoInteractions(secondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_All_Failure_bothStoreEmpty() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(secondaryTorpedoStore, never()).getTorpedoCount();
    verify(secondaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, never()).fire(1);  
    verify(secondaryTorpedoStore, never()).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success_primaryStoreEmpty() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(secondaryTorpedoStore, never()).getTorpedoCount();
    verify(secondaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, never()).fire(1);  
    verify(secondaryTorpedoStore, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Failure_SecondaryEmpty_Primary_fail() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(false);
    when(primaryTorpedoStore.fire(1)).thenReturn(false);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);

    verify(primaryTorpedoStore, never()).getTorpedoCount();
    verify(primaryTorpedoStore, times(1)).isEmpty();
    verify(secondaryTorpedoStore, never()).getTorpedoCount();
    verify(secondaryTorpedoStore, times(1)).isEmpty();
    verify(primaryTorpedoStore, times(1)).fire(1);  
    verify(secondaryTorpedoStore, never()).fire(1);
  }
}
