package com.teammetallurgy.aquaculture.items.meta;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@Interface(iface = "squeek.applecore.api.food.IEdible", modid = "AppleCore")
public class MetaItemFood extends ItemFood {

    ArrayList<SubItemFood> subItems;

    public MetaItemFood() {
        super(0, 0, false);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public int addSubItem(SubItemFood subItem) {
        if (subItems == null)
            subItems = new ArrayList<>();

        subItems.add(subItem);
        return subItems.size() - 1;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int i = MathHelper.clamp(itemStack.getItemDamage(), 0, subItems.size() - 1);
        String uname = subItems.get(i).getUnlocalizedName(itemStack);
        uname = uname.replace(" ", "_");
        return "item." + uname;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemStackList) {
        if (this.isInCreativeTab(tab)) {
            for (SubItemFood subItem : subItems) {
                itemStackList.add(subItem.getItemStack());
            }
        }
    }

    /*
    @Override
    @Method(modid = "AppleCore")
    public FoodValues getFoodValues(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getFoodValues(itemStack);
        }
    
        return null;
    }
    
    @Method(modid = "AppleCore")
    public void onEatenAppleCore(ItemStack itemStack, EntityPlayer player) {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            subItems.get(damage).onEatenAppleCore(itemStack, player);
        }
    }
    */

    // ItemRedirects
    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase player) {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).onEaten(itemStack, world, player);
        }

        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getMaxItemUseDuration(itemStack);
        }

        return 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getItemUseAction(itemStack);
        }

        return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        int damage = itemStack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            ItemStack result = subItems.get(damage).onItemRightClick(itemStack, world, player, hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, result);
        }

        return new ActionResult<>(EnumActionResult.FAIL, itemStack);
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getHealAmount();
        }
        return super.getHealAmount(stack);
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getSaturationModifier();
        }
        return super.getSaturationModifier(stack);
    }

}
