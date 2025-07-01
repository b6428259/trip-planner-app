import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Trip Planner",
  description: "A comprehensive trip planning web application for groups of friends",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="antialiased">
        {children}
      </body>
    </html>
  );
}
