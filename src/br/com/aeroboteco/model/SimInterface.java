package br.com.aeroboteco.model;

public interface SimInterface {
	public boolean isEmVoo();
	public boolean isParkingBrakes();
	public boolean isGearDown();
	public long getTAS();
	public long getIAS();
	public double getGS();
	public long getALT();
	public long getHDG();
    public long getVS();
    public double getLat();
    public double getLon();
    public long getZFW();
    public boolean isEmPausa();
    public int getSimRate();
    public int getVentoDirecao();
    public short getVentoVelocidade();
    public double arredondar(double valor);
}
