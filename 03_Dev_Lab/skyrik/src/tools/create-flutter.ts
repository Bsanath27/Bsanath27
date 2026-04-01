import { z } from "zod";
import { BaseTool } from "../utils/base-tool.js";
import { promises as fs } from "fs";
import { join } from "path";

const CREATE_FLUTTER_TOOL_NAME = "21st_magic_create_flutter";
const CREATE_FLUTTER_TOOL_DESCRIPTION = `
Create a minimal Flutter UI package with theme tokens and a small widget set.
`;

export class CreateFlutterTool extends BaseTool {
  name = CREATE_FLUTTER_TOOL_NAME;
  description = CREATE_FLUTTER_TOOL_DESCRIPTION;

  schema = z.object({
    packageName: z
      .string()
      .describe("Dart package name to create, e.g. magic_ui"),
    outputPath: z
      .string()
      .describe("Absolute path where the Flutter package should be written"),
    primaryColor: z
      .string()
      .optional()
      .describe("Hex primary color, e.g. #1565C0"),
  });

  async execute({ packageName, outputPath, primaryColor }: z.infer<typeof this.schema>) {
    try {
      const pkgDir = join(outputPath, packageName);

      // Create folder structure
      await fs.mkdir(join(pkgDir, "lib", "src", "widgets"), { recursive: true });
      await fs.mkdir(join(pkgDir, "assets"), { recursive: true });

      const color = primaryColor ? primaryColor.replace(/^#/, "") : "1565C0";

      // pubspec.yaml
      const pubspec = `name: ${packageName}
description: A generated Flutter UI package
version: 0.0.1
environment:
  sdk: ">=2.17.0 <3.0.0"

flutter:
  uses-material-design: true
  assets:
    - assets/
`;

      // tokens.json
      const tokens = JSON.stringify(
        {
          colors: {
            primary: `#${color}`,
            background: "#FFFFFF",
            surface: "#F6F7F9",
            text: "#111827",
          },
          typography: {
            fontFamily: "Roboto",
            headline1: { size: 32, weight: 700 },
            body: { size: 16, weight: 400 },
          },
          spacing: { xs: 4, sm: 8, md: 16, lg: 24 },
        },
        null,
        2
      );

      // theme.dart
      const themeDart = `import 'package:flutter/material.dart';
import 'src/tokens.dart';

final ThemeData appTheme = ThemeData(
  primaryColor: Color(0xFF${color}),
  scaffoldBackgroundColor: Color(0xFFFFFFFF),
  textTheme: TextTheme(bodyText2: TextStyle(fontFamily: Tokens.typographyFontFamily)),
);
`;

      // tokens.dart
      const tokensDart = `class Tokens {
  static const String typographyFontFamily = 'Roboto';
  static const int primaryColor = 0xFF${color};
}
`;

      // button.dart
      const buttonDart = `import 'package:flutter/material.dart';
import '../src/tokens.dart';

class MagicButton extends StatelessWidget {
  final String label;
  final VoidCallback? onPressed;

  const MagicButton({super.key, required this.label, this.onPressed});

  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(backgroundColor: const Color(Tokens.primaryColor)),
      onPressed: onPressed,
      child: Text(label),
    );
  }
}
`;

      // card.dart
      const cardDart = `import 'package:flutter/material.dart';

class MagicCard extends StatelessWidget {
  final Widget child;

  const MagicCard({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      margin: const EdgeInsets.all(8),
      child: Padding(padding: const EdgeInsets.all(12), child: child),
    );
  }
}
`;

      // example main.dart
      const mainDart = `import 'package:flutter/material.dart';
import 'package:${packageName}/src/tokens.dart';
import 'package:${packageName}/src/theme.dart';
import 'package:${packageName}/src/widgets/button.dart';
import 'package:${packageName}/src/widgets/card.dart';

void main() {
  runApp(const ExampleApp());
}

class ExampleApp extends StatelessWidget {
  const ExampleApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '${packageName} example',
      theme: appTheme,
      home: Scaffold(
        appBar: AppBar(title: const Text('Magic UI example')),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              MagicCard(child: const Text('Hello from Magic UI')),
              const SizedBox(height: 12),
              MagicButton(label: 'Tap me', onPressed: () {}),
            ],
          ),
        ),
      ),
    );
  }
}
`;

      // Write files
      await fs.writeFile(join(pkgDir, "pubspec.yaml"), pubspec, "utf8");
      await fs.writeFile(join(pkgDir, "tokens.json"), tokens, "utf8");
      await fs.writeFile(join(pkgDir, "lib", "src", "theme.dart"), themeDart, "utf8");
      await fs.writeFile(join(pkgDir, "lib", "src", "tokens.dart"), tokensDart, "utf8");
      await fs.writeFile(join(pkgDir, "lib", "src", "widgets", "button.dart"), buttonDart, "utf8");
      await fs.writeFile(join(pkgDir, "lib", "src", "widgets", "card.dart"), cardDart, "utf8");
      await fs.writeFile(join(pkgDir, "example.dart"), mainDart, "utf8");

      return {
        content: [
          {
            type: "text" as const,
            text: `Flutter package ${packageName} generated at ${pkgDir}`,
          },
        ],
      };
    } catch (error) {
      console.error("Error generating Flutter package", error);
      throw error;
    }
  }
}
