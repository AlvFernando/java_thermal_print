package com.printer.command.printer_command;

import java.io.IOException;

import javax.print.PrintService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class MainController {
    @GetMapping("/")
    public String index(){
        String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
        for(String printServiceName: printServicesNames){
            System.out.println(printServiceName);
        }
        return "Hello World";
    }

    @GetMapping("/printsample")
    public void print(){
        PrintService printService = PrinterOutputStream.getPrintServiceByName("POS58 Printer");
        EscPos escpos;
        try {
            escpos = new EscPos(new PrinterOutputStream(printService));
            
            Style title = new Style()
                    .setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);

            Style subtitle = new Style(escpos.getStyle())
                    .setBold(true)
                    .setUnderline(Style.Underline.OneDotThick);
            Style bold = new Style(escpos.getStyle())
                    .setBold(true);
            
            // escpos.writeLF(title,"Cashier App")
            //         .feed(3)
            //         .write("Client: ")
            //         .writeLF(subtitle, "FUCK YOU")
            //         .feed(3)
            //         .writeLF("FUCK YOU JOHN                      $1.00")
            //         .writeLF("FUCK YOU ALVIN                     $0.50")
            //         .writeLF("----------------------------------------")
            //         .feed(2)
            //         .writeLF(bold, 
            //                  "TOTOTOT                            $1.50")
            //         .writeLF("----------------------------------------")
            //         .feed(8)
            //         .cut(EscPos.CutMode.FULL);

            EscPos customReceipt = escpos.writeLF(title,"Cashier App").feed(3).write("Client: ").writeLF(subtitle, "FUCK YOU").feed(3);
            for(int i=0;i<3;i++){
                customReceipt.writeLF("FUCK YOU "+i+"        $1.00");
            }
            customReceipt.feed(2)
            .writeLF("--------------------------------")
            .writeLF(bold,"TOTOTOT        $1.50")
            .writeLF("--------------------------------")
            .feed(8)
            .cut(EscPos.CutMode.FULL);
            
            escpos.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
