package org.mossmc.mosscg.MossLib;

import org.mossmc.mosscg.MossLib.Command.CommandExample;
import org.mossmc.mosscg.MossLib.Command.CommandManager;
import org.mossmc.mosscg.MossLib.File.FileCheck;
import org.mossmc.mosscg.MossLib.File.FileDependency;
import org.mossmc.mosscg.MossLib.Info.InfoManager;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;
import org.mossmc.mosscg.MossLib.Object.ObjectSQLiteInfo;
import org.mossmc.mosscg.MossLib.SQLite.SQLiteManager;

public class Main {
    /**
     * 主方法是拿来测试用的
     * 没啥实际用途
     * 可以作为实际使用的参考
     */
    public static void main(String[] args) throws Exception {
        //检查文件夹
        FileCheck.checkDirExist("./MossLib");
        //依赖模块初始化
        FileDependency.loadDependencyDir("./MossLib/dependency","dependency");
        //日志模块初始化
        ObjectLogger logger = new ObjectLogger("./MossLib/logs");

        //Mysql模块初始化
        /*
        ObjectMysqlInfo mysqlInfo = new ObjectMysqlInfo();
        mysqlInfo.address = "127.0.0.1";
        mysqlInfo.port = "3306";
        mysqlInfo.database = "minecraft";
        mysqlInfo.username = "test";
        mysqlInfo.password = "password";
        mysqlInfo.poolSize = 10;
        MysqlManager.initMysql(mysqlInfo,logger,true);
        */

        //SQLite模块初始化
        /*
        ObjectSQLiteInfo SQLiteInfo = new ObjectSQLiteInfo();
        SQLiteInfo.filePath = "./MossLib/data.db";
        SQLiteInfo.poolSize = 10;
        SQLiteManager.initSQLite(SQLiteInfo,logger,true);
         */


        //邮箱模块初始化
        /*
        ObjectMailInfo mailInfo = new ObjectMailInfo();
        mailInfo.mailAccount = "12346@163.com";
        mailInfo.mailPassword = "123456";
        mailInfo.mailSMTPHost = "SMTP.163.com";
        mailInfo.mailSMTPPort = "465";
        MailManager.initMail(mailInfo,logger,true);
        ObjectMail mail = new ObjectMail();
        MailManager.sendMail(mail);
         */

        //信息模块初始化
        InfoManager.initSystemInfo(logger,true);

        //指令模块初始化
        CommandManager.initCommand(logger,true);
        //注册指令
        CommandManager.registerCommand(new CommandExample());
    }
}
