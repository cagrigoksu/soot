package soot.baf.internal;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1999 Patrick Lam, Patrick Pominville and Raja Vallee-Rai
 * Copyright (C) 2004 Ondrej Lhotak
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import java.util.List;

import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.VoidType;
import soot.baf.DynamicInvokeInst;
import soot.baf.InstSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;

@SuppressWarnings({ "serial", "unchecked" })
public class BDynamicInvokeInst extends AbstractInvokeInst implements DynamicInvokeInst {
  protected final SootMethodRef bsmRef;
  private final List<Value> bsmArgs;
  protected int tag;

  public BDynamicInvokeInst(SootMethodRef bsmMethodRef, List<Value> bsmArgs, SootMethodRef methodRef, int tag) {
    this.bsmRef = bsmMethodRef;
    this.bsmArgs = bsmArgs;
    this.methodRef = methodRef;
    this.tag = tag;
  }

  public int getInCount() {
    return methodRef.parameterTypes().size();

  }

  public Object clone() {
    return new BDynamicInvokeInst(bsmRef, bsmArgs, methodRef, tag);
  }

  public int getOutCount() {
    if (methodRef.returnType() instanceof VoidType) {
      return 0;
    } else {
      return 1;
    }
  }

  public SootMethodRef getBootstrapMethodRef() {
    return bsmRef;
  }

  public List<Value> getBootstrapArgs() {
    return bsmArgs;
  }

  public String getName() {
    return "dynamicinvoke";
  }

  public void apply(Switch sw) {
    ((InstSwitch) sw).caseDynamicInvokeInst(this);
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();

    buffer.append(Jimple.DYNAMICINVOKE);
    buffer.append(" \"");
    buffer.append(methodRef.name()); // quoted method name (can be any UTF8 string)
    buffer.append("\" <");
    buffer
        .append(SootMethod.getSubSignature(""/* no method name here */, methodRef.parameterTypes(), methodRef.returnType()));
    buffer.append(">");
    buffer.append(bsmRef.getSignature());
    buffer.append("(");
    for (int i = 0; i < bsmArgs.size(); i++) {
      if (i != 0) {
        buffer.append(", ");
      }

      buffer.append(bsmArgs.get(i).toString());
    }
    buffer.append(")");

    return buffer.toString();
  }

  public void toString(UnitPrinter up) {
    up.literal(Jimple.DYNAMICINVOKE);
    up.literal(" \"" + methodRef.name() + "\" <"
        + SootMethod.getSubSignature(""/* no method name here */, methodRef.parameterTypes(), methodRef.returnType())
        + "> ");
    up.methodRef(bsmRef);
    up.literal("(");

    for (int i = 0; i < bsmArgs.size(); i++) {
      if (i != 0) {
        up.literal(", ");
      }

      bsmArgs.get(i).toString(up);
    }

    up.literal(")");
  }

  @Override
  public int getHandleTag() {
    return tag;
  }
}
