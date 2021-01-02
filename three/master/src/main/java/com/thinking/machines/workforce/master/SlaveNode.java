package com.thinking.machines.workforce.master;
import java.nio.file.*;
import com.thinking.machines.workforce.utils.*;
import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;
class SlaveNode
{
private int slaveStarterPortNumber;
private String ip;
private int slavePortNumber;
public void setSlaveStarterPortNumber(int slaveStarterPortNumber)
{
this.slaveStarterPortNumber=slaveStarterPortNumber;
}
public int getSlaveStarterPortNumber()
{
return this.slaveStarterPortNumber; 
}
public void setIp(String ip) 
{
this.ip=ip; 
}
public String getIp() 
{
return this.ip; 
}
public void setSlavePortNumber(int slavePortNumber) 
{
this.slavePortNumber=slavePortNumber; 
}
public int getSlavePortNumber() 
{
return this.slavePortNumber; 
}
}