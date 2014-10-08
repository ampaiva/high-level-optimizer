package com.ampaiva.hlo.cm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.lang.reflect.Method;
import java.util.List;

public class LOCC {
    private final CompilationUnit cu;

    public LOCC(CompilationUnit cu) {
        this.cu = cu;
    }

    public int getMetric() {
        final GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]> mva = new GenericVisitorAdapter<ClassOrInterfaceDeclaration, int[]>() {
            @Override
            public ClassOrInterfaceDeclaration visit(ClassOrInterfaceDeclaration cu, int[] total) {
                total[0] += LOCC.this.visit(cu);
                return null;
            }
        };
        final int[] total = new int[1];
        mva.visit(cu, total);
        return total[0];
    }

    public int visit(ClassOrInterfaceDeclaration classOrInterface) {
        int total = 0;
        total += countEHListBodyDeclaration(classOrInterface.getMembers());
        return total;
    }

    private int countEHListBodyDeclaration(List<BodyDeclaration> bodyDeclList) {
        int total = 0;
        if (bodyDeclList != null) {
            for (BodyDeclaration body : bodyDeclList) {
                try {
                    total += countObject(body);
                } catch (Exception e) {
                    throw new IllegalArgumentException(body.toString() + ": " + e.toString(), e);
                }
            }
        }
        return total;
    }

    private int countObject(Object body) {
        int total = 0;
        if (body != null) {
            try {
                Method m = this.getClass()
                        .getDeclaredMethod("count" + body.getClass().getSimpleName(), body.getClass());
                Integer i = (Integer) m.invoke(this, body);
                total += i;
            } catch (Exception e) {
                throw new IllegalArgumentException(body.toString() + ": " + e.toString(), e);
            }
        }
        return total;
    }

    private int countFieldDeclaration(FieldDeclaration field) {
        return 0;
    }

    private int countConstructorDeclaration(ConstructorDeclaration constructor) {
        int total = 0;
        total += countThrowsinNameExpr(constructor.getThrows());
        total += countBlockStmt(constructor.getBlock());
        return total;
    }

    private int countInitializerDeclaration(InitializerDeclaration initializer) {
        int total = 0;
        total += countBlockStmt(initializer.getBlock());
        return total;
    }

    private int countMethodDeclaration(MethodDeclaration m) {
        int total = 0;
        total += countThrowsinNameExpr(m.getThrows());
        total += countBlockStmt(m.getBody());

        return total;
    }

    private int countThrowsinNameExpr(List<NameExpr> throws_) {
        int total = 0;
        if (throws_ == null) {
            return 0;
        }
        total += 1 + throws_.get(throws_.size() - 1).getBeginLine() - throws_.get(0).getBeginLine();
        return total;
    }

    private int countBlockStmt(BlockStmt block) {
        if (block == null) {
            return 0;
        }
        return countTryLines(block.getStmts());

    }

    private int countTryLines(List<Statement> stmts) {
        int count = 0;
        if (stmts != null) {
            for (Statement stmt : stmts) {
                count += countEHStatement(stmt);
            }
        }
        return count;
    }

    private int countEHStatement(Statement stmt) {
        int count = 0;
        if (stmt instanceof ThrowStmt) {
            count += 1 + stmt.getEndLine() - stmt.getBeginLine();
        } else if (stmt instanceof TryStmt) {
            count += countTryStatement((TryStmt) stmt);
        } else if (stmt instanceof ReturnStmt) {
            count += countExpression(((ReturnStmt) stmt).getExpr());
        } else if (stmt instanceof SynchronizedStmt) {
            count += countExpression(((SynchronizedStmt) stmt).getExpr())
                    + countBlockStmt(((SynchronizedStmt) stmt).getBlock());
        } else if (stmt instanceof BreakStmt || stmt instanceof ContinueStmt) {
        } else if (stmt instanceof DoStmt) {
            count += countEHStatement(((DoStmt) stmt).getBody());
            count += countExpression(((DoStmt) stmt).getCondition());
        } else if (stmt instanceof WhileStmt) {
            count += countEHStatement(((WhileStmt) stmt).getBody());
            count += countExpression(((WhileStmt) stmt).getCondition());
        } else if (stmt instanceof ForStmt) {
            count += countEHListExpressions(((ForStmt) stmt).getInit());
            count += countExpression(((ForStmt) stmt).getCompare());
            count += countEHListExpressions(((ForStmt) stmt).getUpdate());
            count += countEHStatement(((ForStmt) stmt).getBody());
        } else if (stmt instanceof SwitchStmt) {
            count += countExpression(((SwitchStmt) stmt).getSelector());
            count += countEHSwitchEntries(((SwitchStmt) stmt).getEntries());
        } else if (stmt instanceof BlockStmt) {
            count += countBlockStmt((BlockStmt) stmt);
        } else if (stmt instanceof ExpressionStmt) {
            count += countExpression(((ExpressionStmt) stmt).getExpression());
        } else if (stmt instanceof IfStmt) {
            count += countEHStatement(((IfStmt) stmt).getThenStmt());
            count += countEHStatement(((IfStmt) stmt).getElseStmt());
        } else if (stmt != null) {
            count += countObject(stmt);
        }
        return count;
    }

    private int countExplicitConstructorInvocationStmt(
            ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt) {
        int total = 0;
        total += countObject(explicitConstructorInvocationStmt.getExpr());
        return total;
    }

    private int countEHSwitchEntries(List<SwitchEntryStmt> entries) {
        int total = 0;
        for (SwitchEntryStmt switchEntryStmt : entries) {
            total += countExpression(switchEntryStmt.getLabel());
            total += countTryLines(switchEntryStmt.getStmts());
        }
        return total;
    }

    private int countEHListExpressions(List<Expression> dimensions) {
        int count = 0;
        if (dimensions != null) {
            for (Expression expression : dimensions) {
                count += countExpression(expression);
            }
        }
        return count;
    }

    private int countExpression(Expression expression) {
        if (expression == null) {
            return 0;
        } else if (expression instanceof MethodCallExpr) {
            return 0;
        } else if (expression instanceof VariableDeclarationExpr) {
            return 0;
        } else if (expression instanceof ThisExpr) {
            return countExpression(((ThisExpr) expression).getClassExpr());
        } else if (expression instanceof EnclosedExpr) {
            return countExpression(((EnclosedExpr) expression).getInner());
        } else if (expression instanceof ArrayCreationExpr) {
            return countExpression(((ArrayCreationExpr) expression).getInitializer())
                    + countEHListExpressions(((ArrayCreationExpr) expression).getDimensions());
        } else if (expression instanceof ObjectCreationExpr) {
            return countExpression(((ObjectCreationExpr) expression).getScope())
                    + countEHListExpressions(((ObjectCreationExpr) expression).getArgs())
                    + countEHListBodyDeclaration(((ObjectCreationExpr) expression).getAnonymousClassBody());
        } else if (expression instanceof ArrayAccessExpr) {
            return countExpression(((ArrayAccessExpr) expression).getIndex())
                    + countExpression(((ArrayAccessExpr) expression).getName());
        } else if (expression instanceof FieldAccessExpr) {
            return countExpression(((FieldAccessExpr) expression).getScope());
        } else if (expression instanceof NameExpr) {
            return 0;
        } else if (expression instanceof NullLiteralExpr) {
            return 0;
        } else if (expression instanceof BooleanLiteralExpr) {
            return 0;
        } else if (expression instanceof BinaryExpr) {
            return countExpression(((BinaryExpr) expression).getLeft())
                    + countExpression(((BinaryExpr) expression).getRight());
        } else if (expression instanceof UnaryExpr) {
            return countExpression(((UnaryExpr) expression).getExpr());
        } else if (expression instanceof StringLiteralExpr) {
            return 0;
        } else if (expression instanceof ConditionalExpr) {
            return countExpression(((ConditionalExpr) expression).getCondition())
                    + countExpression(((ConditionalExpr) expression).getThenExpr())
                    + countExpression(((ConditionalExpr) expression).getElseExpr());
        } else if (expression instanceof CastExpr) {
            return countExpression(((CastExpr) expression).getExpr());
        } else if (expression instanceof AssignExpr) {
            return countExpression(((AssignExpr) expression).getValue())
                    + countExpression(((AssignExpr) expression).getTarget());
        }
        throw new IllegalArgumentException(expression.toString());
    }

    private int countTryStatement(TryStmt tryStmt) {
        int count = 0;
        count += 1 + tryStmt.getEndLine() - tryStmt.getBeginLine();
        // Block of code inside try block excluding } (end line) and { (begin line)
        int delta = (tryStmt.getTryBlock().getEndLine() - 1) - (tryStmt.getTryBlock().getBeginLine());
        if (delta > 0) {
            count -= delta;
        }
        count += countTryLines(tryStmt.getTryBlock().getStmts());
        return count;
    }
}
