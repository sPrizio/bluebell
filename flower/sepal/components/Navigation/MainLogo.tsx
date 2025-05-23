"use client";

import Image from "next/image";
import brandTertiary from "../../app/assets/brand/bluebell/bluebell_tertiary.png";
import brandWhite from "../../app/assets/brand/bluebell/bluebell_white.png";
import brandPrimary from "../../app/assets/brand/bluebell/bluebell_primary.png";
import { useEffect, useState } from "react";
import Link from "next/link";

/**
 * Renders the main logo of the app
 *
 * @param variant color variant
 * @param size logo size
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MainLogo({
  variant = "primary",
  size = 65,
}: Readonly<{
  variant?: "primary" | "secondary" | "tertiary" | "white" | "transparent";
  size?: number;
}>) {
  const [windowSize, setWindowSize] = useState([0, 0]);

  useEffect(() => {
    window.addEventListener("resize", updateSize);
    updateSize();
    return () => window.removeEventListener("resize", updateSize);
  }, []);

  //  FUNCTIONS

  /**
   * Updates the window size
   */
  function updateSize() {
    setWindowSize([window.innerWidth, window.innerHeight]);
  }

  /**
   * Computes the image depending on the variant
   */
  function determineImage(): any {
    switch (variant) {
      case "primary":
        return brandPrimary;
      case "secondary":
        return brandTertiary;
      default:
        return brandWhite;
    }
  }

  //  RENDER

  return (
    <Link href={"/home"}>
      <Image
        src={determineImage()}
        height={windowSize[0] < 992 ? 50 : size}
        alt={"Brand Logo"}
      />
    </Link>
  );
}
