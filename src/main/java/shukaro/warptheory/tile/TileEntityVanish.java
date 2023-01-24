package shukaro.warptheory.tile;

//public class TileEntityVanish extends TileEntity
//{
 //   private NameMetaPair pair;
 //   private NBTTagCompound tag;
 //   private long returnTime;

 //   public TileEntityVanish(World world, int x, int y, int z, long returnTime)
 //   {
 //       this.pair = new NameMetaPair(world.getBlockState(new BlockPos(x, y, z)).getBlock(), world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z))));
 //       if (world.getTileEntity(new BlockPos(x, y, z)) != null)
 //       {
 //           this.tag = new NBTTagCompound();
   //         world.getTileEntity(new BlockPos(x, y, z)).writeToNBT(this.tag);
     //   }
       // this.returnTime = returnTime;
//}
//
  //  @Override
    //public boolean canUpdate() { return true; }
//
  //  @Override
    //public void onUpdate()
   // {
     //   if (this.world.getTotalWorldTime() >= this.returnTime)
       //     rebuildBlock();
  //  }
//
// private void rebuildBlock()
//  {
//       this.world.setBlock(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.pair.getBlock(), this.pair.getMetadata(), 0);
//       if (this.tag != null)
//          this.world.getTileEntity(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()).readFromNBT(this.tag);
//      this.world.markBlockForUpdate(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
//  }

//   @Override
//   public void writeToNBT(NBTTagCompound tag)
//   {
//       super.writeToNBT(tag);
//       tag.setString("oldName", this.pair.getName());
//       tag.setInteger("oldMeta", this.pair.getMetadata());
//       tag.setTag("oldTag", this.tag);
//       tag.setLong("returnTime", this.returnTime);
//   }

//  @Override
//   public void readFromNBT(NBTTagCompound tag)
//   {
   //     super.readFromNBT(tag);
  //      this.pair = new NameMetaPair(tag.getString("oldName"), tag.getInteger("oldMeta"));
  //      if (tag.hasKey("oldTag"))
  //          this.tag = tag.getCompoundTag("oldTag");
  //      this.returnTime = tag.getLong("returnTime");
  //  }

  //  @Override
  //  public Packet getDescriptionPacket()
  //  {
  //      NBTTagCompound tag = new NBTTagCompound();
  //      this.writeToNBT(tag);
  //      return new SPacketUpdateTileEntity(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), -999, tag);
  //  }

  //  @Override
  //  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
  //  {
   //     super.onDataPacket(net, pkt);
    //    this.readFromNBT(pkt.getNbtCompound());
   // }
//}
